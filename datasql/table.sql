-- =====================================================
-- WEB XEM PHIM ONLINE - PostgreSQL / Supabase
-- Bản đầy đủ: 21 bảng, không thanh toán, có phân quyền admin
-- ĐÃ ÁP DỤNG 6 FIX NGHIÊM TRỌNG (xem comment [FIX x])
-- Chạy 1 lần trong SQL Editor.
-- =====================================================

-- ============ ENUMS ============
create type movie_type    as enum ('movie', 'series');
create type movie_status  as enum ('draft', 'published', 'hidden', 'upcoming');
create type user_role     as enum ('user', 'moderator', 'admin');
create type video_quality as enum ('360p', '480p', '720p', '1080p', '2k', '4k');
create type report_status as enum ('pending', 'reviewed', 'resolved', 'rejected');
create type person_role   as enum ('actor', 'director', 'writer', 'producer');


-- =====================================================
-- 1. NGƯỜI DÙNG
-- =====================================================
create table profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  username text unique,
  display_name text,
  avatar_url text,
  role user_role not null default 'user',
  is_banned boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- Tự tạo profile khi có người đăng ký
create or replace function handle_new_user() returns trigger
language plpgsql security definer set search_path = public as $$
begin
  insert into profiles (id, display_name, avatar_url)
  values (new.id, new.raw_user_meta_data->>'full_name', new.raw_user_meta_data->>'avatar_url');
  return new;
end; $$;

create trigger on_auth_user_created
after insert on auth.users
for each row execute function handle_new_user();

-- Supabase Auth Hook: đưa role do server quản lý vào app_metadata của access token.
-- Bật hàm này tại Authentication > Hooks > Custom Access Token trong Supabase.
create or replace function public.custom_access_token_hook(event jsonb)
returns jsonb
language plpgsql
stable
as $$
declare
  claims jsonb;
  profile_role text;
begin
  select role::text into profile_role
  from public.profiles
  where id = (event->>'user_id')::uuid;

  claims := event->'claims';
  claims := jsonb_set(claims, '{app_metadata}', coalesce(claims->'app_metadata', '{}'::jsonb), true);
  claims := jsonb_set(claims, '{app_metadata,role}', to_jsonb(coalesce(profile_role, 'user')), true);
  return jsonb_set(event, '{claims}', claims, true);
end;
$$;

grant usage on schema public to supabase_auth_admin;
grant select on table public.profiles to supabase_auth_admin;
grant execute on function public.custom_access_token_hook(jsonb) to supabase_auth_admin;
revoke execute on function public.custom_access_token_hook(jsonb) from public, anon, authenticated;


-- ============ HÀM KIỂM TRA QUYỀN ============
create or replace function is_admin() returns boolean
language sql stable security definer set search_path = public as $$
  select exists (select 1 from profiles where id = auth.uid() and role = 'admin');
$$;

create or replace function is_staff() returns boolean
language sql stable security definer set search_path = public as $$
  select exists (select 1 from profiles where id = auth.uid() and role in ('admin','moderator'));
$$;

-- [FIX 1] Chặn user tự đổi role / tự gỡ ban
-- Thêm điều kiện auth.uid() is not null để SQL Editor / service_role
-- có thể tạo admin đầu tiên (auth.uid() = NULL khi chạy từ SQL Editor).
create or replace function prevent_role_change() returns trigger
language plpgsql security definer set search_path = public as $$
begin
  if (new.role is distinct from old.role or new.is_banned is distinct from old.is_banned)
     and auth.uid() is not null
     and not is_admin() then
    raise exception 'Chỉ admin mới được đổi role / ban user';
  end if;
  return new;
end; $$;

create trigger protect_profile_role
before update on profiles
for each row execute function prevent_role_change();


-- =====================================================
-- 2. DANH MỤC
-- =====================================================
create table genres (
  id serial primary key,
  name text not null unique,
  slug text not null unique
);

create table countries (
  id serial primary key,
  name text not null unique,
  slug text not null unique,
  code char(2)
);

create table people (
  id bigserial primary key,
  name text not null,
  slug text not null unique,
  avatar_url text,
  bio text,
  birth_date date
);


-- =====================================================
-- 3. PHIM
-- =====================================================
create table movies (
  id bigserial primary key,
  type movie_type not null default 'movie',
  title text not null,
  original_title text,
  slug text not null unique,
  description text,
  poster_url text,
  backdrop_url text,
  trailer_url text,
  release_year int,
  duration_minutes int,
  age_rating text,
  country_id int references countries(id),
  status movie_status not null default 'draft',
  view_count bigint not null default 0,
  -- [FIX 2] numeric(4,2) thay vì numeric(3,2) để chứa được điểm 10.00
  rating_avg numeric(4,2) not null default 0,
  rating_count int not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index on movies (status, created_at desc);
create index on movies (type);
create index on movies (release_year);
create index on movies (country_id);
create index movies_search_idx on movies
  using gin (to_tsvector('simple', coalesce(title,'') || ' ' || coalesce(original_title,'')));

create table movie_genres (
  movie_id bigint references movies(id) on delete cascade,
  genre_id int references genres(id) on delete cascade,
  primary key (movie_id, genre_id)
);

create table movie_people (
  movie_id bigint references movies(id) on delete cascade,
  person_id bigint references people(id) on delete cascade,
  role person_role not null,
  character_name text,
  sort_order int default 0,
  primary key (movie_id, person_id, role)
);


-- =====================================================
-- 4. MÙA & TẬP
-- =====================================================
create table seasons (
  id bigserial primary key,
  movie_id bigint not null references movies(id) on delete cascade,
  season_number int not null,
  title text,
  unique (movie_id, season_number)
);

create table episodes (
  id bigserial primary key,
  movie_id bigint not null references movies(id) on delete cascade,
  season_id bigint references seasons(id) on delete cascade,
  episode_number int not null,
  title text,
  description text,
  thumbnail_url text,
  duration_minutes int,
  published_at timestamptz default now(),
  view_count bigint not null default 0,
  unique nulls not distinct (movie_id, season_id, episode_number)
);
create index on episodes (movie_id);


-- =====================================================
-- 5. VIDEO & PHỤ ĐỀ
-- =====================================================
create table video_sources (
  id bigserial primary key,
  episode_id bigint not null references episodes(id) on delete cascade,
  quality video_quality not null default '720p',
  url text not null,
  server_name text default 'Server 1',
  is_active boolean not null default true,
  created_at timestamptz not null default now()
);
create index on video_sources (episode_id);

create table subtitles (
  id bigserial primary key,
  episode_id bigint not null references episodes(id) on delete cascade,
  language_code text not null,
  label text not null,
  url text not null,
  is_default boolean not null default false
);
create index on subtitles (episode_id);


-- =====================================================
-- 6. TƯƠNG TÁC NGƯỜI DÙNG
-- =====================================================
create table watch_history (
  id bigserial primary key,
  user_id uuid not null references profiles(id) on delete cascade,
  movie_id bigint not null references movies(id) on delete cascade,
  episode_id bigint not null references episodes(id) on delete cascade,
  progress_seconds int not null default 0,
  duration_seconds int,
  is_completed boolean not null default false,
  last_watched_at timestamptz not null default now(),
  unique (user_id, episode_id)
);
create index on watch_history (user_id, last_watched_at desc);

create table favorites (
  user_id uuid references profiles(id) on delete cascade,
  movie_id bigint references movies(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (user_id, movie_id)
);

create table ratings (
  user_id uuid references profiles(id) on delete cascade,
  movie_id bigint references movies(id) on delete cascade,
  score smallint not null check (score between 1 and 10),
  created_at timestamptz not null default now(),
  primary key (user_id, movie_id)
);

create table comments (
  id bigserial primary key,
  user_id uuid not null references profiles(id) on delete cascade,
  movie_id bigint not null references movies(id) on delete cascade,
  episode_id bigint references episodes(id) on delete cascade,
  parent_id bigint references comments(id) on delete cascade,
  content text not null,
  is_hidden boolean not null default false,
  created_at timestamptz not null default now()
);
create index on comments (movie_id, created_at desc);

create table comment_likes (
  user_id uuid references profiles(id) on delete cascade,
  comment_id bigint references comments(id) on delete cascade,
  primary key (user_id, comment_id)
);

create table reports (
  id bigserial primary key,
  user_id uuid references profiles(id) on delete set null,
  movie_id bigint references movies(id) on delete cascade,
  episode_id bigint references episodes(id) on delete cascade,
  comment_id bigint references comments(id) on delete cascade,
  reason text not null,
  status report_status not null default 'pending',
  created_at timestamptz not null default now()
);

create table notifications (
  id bigserial primary key,
  user_id uuid not null references profiles(id) on delete cascade,
  title text not null,
  body text,
  link text,
  is_read boolean not null default false,
  created_at timestamptz not null default now()
);
create index on notifications (user_id, is_read, created_at desc);


-- =====================================================
-- 7. QUẢN TRỊ / HIỂN THỊ
-- =====================================================
create table banners (
  id serial primary key,
  movie_id bigint references movies(id) on delete cascade,
  image_url text not null,
  title text,
  link text,
  sort_order int default 0,
  is_active boolean not null default true
);

create table collections (
  id serial primary key,
  name text not null,
  slug text not null unique,
  description text,
  sort_order int default 0,
  is_active boolean not null default true
);

create table collection_movies (
  collection_id int references collections(id) on delete cascade,
  movie_id bigint references movies(id) on delete cascade,
  sort_order int default 0,
  primary key (collection_id, movie_id)
);


-- =====================================================
-- FIX 6.1: Trigger tự cập nhật rating_avg / rating_count
-- Mỗi khi có rating thêm/sửa/xóa.
-- =====================================================
create or replace function refresh_movie_rating() returns trigger
language plpgsql security definer set search_path = public as $$
declare
  v_movie_id bigint;
begin
  v_movie_id := coalesce(new.movie_id, old.movie_id);

  update movies set
    rating_avg   = coalesce(
                     (select round(avg(score)::numeric, 2)
                      from ratings where movie_id = v_movie_id),
                     0),
    rating_count = (select count(*) from ratings where movie_id = v_movie_id)
  where id = v_movie_id;

  return null;
end; $$;

create trigger trg_rating_change
after insert or update or delete on ratings
for each row execute function refresh_movie_rating();


-- =====================================================
-- FIX 6.2: Trigger tự cập nhật updated_at
-- =====================================================
create or replace function set_updated_at() returns trigger
language plpgsql as $$
begin
  new.updated_at = now();
  return new;
end; $$;

create trigger trg_profiles_updated
before update on profiles
for each row execute function set_updated_at();

create trigger trg_movies_updated
before update on movies
for each row execute function set_updated_at();


-- =====================================================
-- RLS (PHÂN QUYỀN)
-- =====================================================

-- ---- Bảng công khai đơn giản: đọc ai cũng được, ghi chỉ admin ----
-- (episodes, subtitles, banners, collections KHÔNG nằm trong loop này
--  vì cần policy custom bên dưới)
do $$
declare t text;
begin
  foreach t in array array[
    'genres','countries','people','movie_genres','movie_people',
    'seasons','collection_movies'
  ] loop
    execute format('alter table %I enable row level security', t);
    execute format('create policy "public read %1$s" on %1$I for select using (true)', t);
    execute format('create policy "admin write %1$s" on %1$I for all using (is_admin()) with check (is_admin())', t);
  end loop;
end $$;


-- ---- movies: chỉ phim published/upcoming mới công khai ----
alter table movies enable row level security;
create policy "public read movies" on movies for select
  using (status in ('published','upcoming') or is_admin());
create policy "admin write movies" on movies for all
  using (is_admin()) with check (is_admin());


-- =====================================================
-- [FIX 3] episodes: chỉ lộ tập của phim published/upcoming
-- =====================================================
alter table episodes enable row level security;
create policy "public read episodes" on episodes for select
using (
  is_admin()
  or exists (
    select 1 from movies m
    where m.id = episodes.movie_id
      and m.status in ('published','upcoming')
  )
);
create policy "admin write episodes" on episodes for all
  using (is_admin()) with check (is_admin());


-- =====================================================
-- [FIX 3] video_sources: chỉ lộ khi is_active + phim published
-- =====================================================
alter table video_sources enable row level security;
create policy "public read video_sources" on video_sources for select
using (
  is_admin()
  or (
    is_active
    and exists (
      select 1 from episodes e
      join movies m on m.id = e.movie_id
      where e.id = video_sources.episode_id
        and m.status in ('published','upcoming')
    )
  )
);
create policy "admin write video_sources" on video_sources for all
  using (is_admin()) with check (is_admin());


-- =====================================================
-- [FIX 3] subtitles: chỉ lộ phụ đề của phim published
-- =====================================================
alter table subtitles enable row level security;
create policy "public read subtitles" on subtitles for select
using (
  is_admin()
  or exists (
    select 1 from episodes e
    join movies m on m.id = e.movie_id
    where e.id = subtitles.episode_id
      and m.status in ('published','upcoming')
  )
);
create policy "admin write subtitles" on subtitles for all
  using (is_admin()) with check (is_admin());


-- =====================================================
-- [FIX 4] banners: bỏ is_active = false khỏi public read
-- =====================================================
alter table banners enable row level security;
create policy "public read banners" on banners for select
  using (is_active or is_admin());
create policy "admin write banners" on banners for all
  using (is_admin()) with check (is_admin());


-- =====================================================
-- [FIX 4] collections: bỏ is_active = false khỏi public read
-- =====================================================
alter table collections enable row level security;
create policy "public read collections" on collections for select
  using (is_active or is_admin());
create policy "admin write collections" on collections for all
  using (is_admin()) with check (is_admin());


-- ---- profiles ----
alter table profiles enable row level security;
create policy "read profiles" on profiles for select using (true);
create policy "update own profile" on profiles for update
  using (id = auth.uid()) with check (id = auth.uid());
create policy "admin update profiles" on profiles for update using (is_admin());


-- ---- Dữ liệu cá nhân: chỉ chủ sở hữu ----
-- (notifications tách riêng bên dưới theo FIX 5)
do $$
declare t text;
begin
  foreach t in array array['watch_history','favorites','ratings'] loop
    execute format('alter table %I enable row level security', t);
    execute format('create policy "own %1$s" on %1$I for all using (user_id = auth.uid()) with check (user_id = auth.uid())', t);
  end loop;
end $$;


-- =====================================================
-- [FIX 5] notifications: user KHÔNG được tự insert
-- - select/update/delete: chủ sở hữu
-- - insert: chỉ staff (admin/moderator) qua policy "admin send"
-- =====================================================
alter table notifications enable row level security;

create policy "own read notifications" on notifications
  for select using (user_id = auth.uid());

create policy "own update notifications" on notifications
  for update using (user_id = auth.uid());

create policy "own delete notifications" on notifications
  for delete using (user_id = auth.uid());

-- Chỉ staff mới được tạo notification cho user
create policy "admin send notifications" on notifications
  for insert with check (is_staff());


-- ---- comments ----
alter table comments enable row level security;
create policy "read comments" on comments for select using (not is_hidden or is_staff());
create policy "insert own comments" on comments for insert
  with check (user_id = auth.uid() and not exists (select 1 from profiles where id = auth.uid() and is_banned));
create policy "update own comments" on comments for update using (user_id = auth.uid());
create policy "staff update comments" on comments for update using (is_staff());
create policy "delete own or staff comments" on comments for delete
  using (user_id = auth.uid() or is_staff());


-- ---- comment_likes ----
alter table comment_likes enable row level security;
create policy "read comment likes" on comment_likes for select using (true);
create policy "own comment likes" on comment_likes for all
  using (user_id = auth.uid()) with check (user_id = auth.uid());


-- ---- reports ----
alter table reports enable row level security;
create policy "user create report" on reports for insert with check (user_id = auth.uid());
create policy "own or staff read reports" on reports for select
  using (user_id = auth.uid() or is_staff());
create policy "staff update reports" on reports for update using (is_staff());
create policy "staff delete reports" on reports for delete using (is_staff());


-- =====================================================
-- TẠO ADMIN ĐẦU TIÊN
-- Đăng ký 1 tài khoản trên web trước, rồi chạy (đổi email):
--
-- update profiles set role = 'admin'
-- where id = (select id from auth.users where email = 'email-cua-ban@gmail.com');
-- =====================================================
