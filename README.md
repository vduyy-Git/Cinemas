# Cinemas

Backend xem phim dùng Spring Boot 4, Java 17, PostgreSQL và Supabase Auth. Frontend hiện vẫn là scaffold Vite và chưa được thay đổi.

## Yêu cầu

- Java 17+
- Maven (hoặc Maven Wrapper trong `backend/`)
- Supabase project đã chạy schema trong `datasql/table.sql`
- JDBC connection string, database credentials và JWT signing secret của Supabase

Schema SQL tham chiếu `auth.users`, `auth.uid()` và RLS của Supabase nên cần chạy trong SQL Editor của Supabase, không chạy trực tiếp trên PostgreSQL cài mới. Tài khoản DB của backend phải được giới hạn quyền theo môi trường triển khai; ứng dụng kiểm tra quyền ở Spring Security vì các kết nối server thường không mang JWT người dùng để thực thi RLS theo từng request.

## Cấu hình và chạy backend

PowerShell (thay các giá trị bằng thông tin dự án của bạn; không commit secrets):

```powershell
$env:DATABASE_URL = 'jdbc:postgresql://<host>:5432/postgres?sslmode=require'
$env:DATABASE_USERNAME = 'postgres.<project-ref>'
$env:DATABASE_PASSWORD = '<database-password>'
$env:SUPABASE_JWT_SECRET = '<legacy-HS256-JWT-secret>'
$env:CORS_ALLOWED_ORIGINS = 'http://localhost:5173'
cd backend
./mvnw.cmd spring-boot:run
```

`SUPABASE_JWT_SECRET` là signing secret HS256 của dự án; token phải có `app_metadata.role` bằng `admin`, `moderator` hoặc `user`. Schema cung cấp hàm Auth Hook `custom_access_token_hook`; bật hàm tại Authentication → Hooks → Custom Access Token trong Supabase. Không dùng `user_metadata` để cấp quyền vì người dùng có thể tự sửa metadata đó. Khi dùng Supabase signing keys bất đối xứng, cần cấu hình xác minh JWKS trước khi bật API ghi.

Hibernate chạy `ddl-auto=validate`: schema cần tồn tại và không bị ứng dụng tự ý sửa. `CORS_ALLOWED_ORIGINS` nhận danh sách origin phân tách bằng dấu phẩy. Cổng mặc định là `8080`.

## API đã có

- `GET /movies?q=&type=movie|series&year=&countryId=&page=0&size=20` — tìm kiếm, lọc và phân trang phim công khai.
- `GET /movies/{slug}` — chi tiết phim đang công khai hoặc sắp chiếu.
- `GET /genres`, `GET /countries` — danh mục.
- `GET /movies/{movieId}/episodes`, `GET /episodes/{episodeId}/playback` — tập, nguồn phát đang hoạt động và phụ đề của phim đã phát hành.
- `GET /movies/{movieId}/comments`, `POST /movies/{movieId}/comments`, `DELETE /movies/{movieId}/comments/{commentId}` — bình luận phân trang; người dùng chỉ xóa bình luận của mình.
- `POST /reports`, `GET/PATCH /admin/reports` — gửi báo cáo, nhân viên xử lý trạng thái.
- `GET /notifications`, `GET /notifications/unread-count`, `PATCH /notifications/{id}/read` — thông báo riêng của tài khoản.
- `GET /me/favorites`, `POST/DELETE /me/favorites/{movieId}` — danh sách yêu thích.
- `PUT /me/ratings/{movieId}` với `{ "score": 1..10 }` — đánh giá (DB trigger cập nhật điểm trung bình).
- `GET /me/watch-history`, `PUT /me/watch-history/{episodeId}` — lưu tiến độ xem.
- `GET /actuator/health` — health check.
- `POST /movies`, `PUT /movies/{id}` — admin/moderator tạo và cập nhật phim.
- `DELETE /movies/{id}` — chỉ admin xóa phim.

API trả dữ liệu theo dạng `{ success, message, data, timestamp }`; danh sách phim dùng `Page` của Spring Data. Gửi access token Supabase qua `Authorization: Bearer <access-token>` cho thao tác quản trị.

## Database và model

`datasql/table.sql` là nguồn schema PostgreSQL/Supabase (21 bảng, enum, trigger, index và RLS). Các entity JPA ánh xạ vào schema đó. Backend hiện có catalog phim, playback, bình luận, báo cáo, thông báo, yêu thích, đánh giá và lịch sử xem. Banners, bộ sưu tập và CRUD quản trị sâu cho danh mục/tập/video chưa có API nghiệp vụ.

## Kiểm tra

Từ thư mục `backend`, dùng `./mvnw.cmd -DskipTests compile` để biên dịch. Khởi chạy ứng dụng cần database đã cài schema và đủ biến môi trường ở trên.
