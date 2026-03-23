🍽️ Hệ Thống Quản Lý Nhà Hàng 

Dự án phần mềm quản lý nhà hàng áp dụng mô hình 3 lớp (GUI - BUS - DAO).

## 🛠 1. Yêu cầu hệ thống (Prerequisites)
Trước khi chạy dự án, đảm bảo máy tính của bạn đã cài đặt:
* **Java JDK:** Phiên bản 17 trở lên.
* **Hệ quản trị CSDL:** SQL Server

---

## 🚀 2. Hướng dẫn Clone và Thiết lập dự án

### Bước 1: Tải mã nguồn về máy
Mở Terminal / Git Bash tại thư mục bạn muốn lưu code và chạy tuần tự các lệnh sau:
```bash
# 1. Clone code từ kho chứa về máy
git clone https://github.com/jimdabest/QuanLyNhaHang.git

# 2. Di chuyển vào thư mục dự án
cd QuanLyNhaHang

# 3. Chuyển sang nhánh làm việc chung của nhóm (BẮT BUỘC)
git checkout develop
```
### Bước 2: Khởi tạo Cơ sở dữ liệu (Database)
Mở phần mềm DBeaver

Mở file script SQL của nhóm (file chứa các lệnh CREATE DATABASE, tạo bảng và dữ liệu mẫu).

Bôi đen toàn bộ lệnh và nhấn (Execute).

Chạy tiếp các file script tạo View, Function và Stored Procedure (nếu có).

### Bước 3: Nạp thư viện JDBC (Tránh lỗi ClassNotFoundException)

#### Trên IntelliJ IDEA:

- Ở cột Project bên trái, mở thư mục `lib`.
- Click chuột phải vào file `mssql-jdbc-xxx.jar`.
- Chọn `Add as Library...` -> Nhấn OK.

#### Nếu dùng VS Code / Google Antigravity:
1. Đảm bảo phần mềm của bạn đã cài đặt Extension **"Extension Pack for Java"**.
2. Nhìn sang thanh công cụ bên trái (Explorer), cuộn xuống dưới cùng và mở rộng tab **JAVA PROJECTS**.
3. Mở rộng tên dự án của bạn (VD: `QuanLyNhaHang`), tìm đến mục **Referenced Libraries**.
4. Bấm vào biểu tượng dấu **`+`** (Add Jar/Zip) nằm ngay cạnh chữ **Referenced Libraries**.
5. Cửa sổ chọn file hiện ra, bạn tìm đến thư mục `lib/` trong dự án và chọn file `mssql-jdbc-xxx.jar` -> Bấm **Select Jar Files**.

#### Trên Eclipse:

- Click chuột phải vào tên Project -> Build Path -> Configure Build Path.
- Chọn tab Libraries -> Classpath -> Add JARs... -> Trỏ tới file .jar trong thư mục lib.



### Bước 4: Cấu hình tài khoản kết nối Database
Mở file `src/quanlynhahang/dao/DBConnection.java`.
Tại phần đầu của file, hãy sửa lại giá trị PASSWORD cho khớp với tài khoản SQL Server trên máy của bạn:

```Java
private static final String SERVER = "localhost";
private static final String PORT = "1433";
private static final String DATABASE = "QuanLyNhaHang";
private static final String USER = "sa"; // User mặc định của SQL Server
private static final String PASSWORD = "mật_khẩu_của_bạn_ở_đây"; // ⚠️ BẮT BUỘC ĐỔI
(Lưu ý: Không commit/push file DBConnection.java lên lại Git nếu bạn chỉ đổi mật khẩu cá nhân, để tránh ghi đè mật khẩu của người khác).
```
## 🧪 3. Chạy thử kết nối (Test Connection)
- Mở file src/test/TestDBConnection.java.
- Chạy (Run) trực tiếp hàm main có trong file này.
- Nếu Console in ra: ✅ KẾT NỐI CƠ SỞ DỮ LIỆU THÀNH CÔNG! -> Bạn đã setup xong! Sẵn sàng code.
- Nếu báo lỗi đỏ (Login failed, TCP/IP port...), hãy kiểm tra lại Bước 4 hoặc xem SQL Server đã bật giao thức TCP/IP trong Configuration Manager chưa.

## ⚠️ 4. Quy tắc làm việc nhóm (Git Workflow)
### Để dự án không bị "sập" do đụng độ code (Conflict), mọi thành viên phải tuân thủ Thần chú 4 bước:
1. Lấy code mới về: `git pull origin develop` (Luôn chạy lệnh này ĐẦU TIÊN trước khi bắt đầu code).
2. Thêm thay đổi: `git add <file cần up>`  
3. Lưu thay đổi: `git commit -m "Tính năng: gì gì gì"` (Ghi chú rõ ràng tiếng Việt).
4. Đẩy code lên: `git push origin develop` 

**❌ Không ai được phép push thẳng lên nhánh main. Nhánh main chỉ dùng để nộp bài cuối kỳ.**