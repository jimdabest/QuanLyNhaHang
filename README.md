# QuanLyNhaHang - He thong POS nha hang

Ung dung desktop Java Swing quan ly nha hang theo kien truc 3 lop GUI - BUS - DAO.

## 1. Tong quan ung dung

Ung dung phuc vu day du cac nghiep vu chinh trong nha hang:

- Quan ly so do ban: trang thai Trong, Dang an, Da dat.
- Dat ban va nhan ban theo so dien thoai khach.
- Goi mon theo ban va cap nhat hoa don theo thoi gian thuc.
- Thanh toan hoa don, ap dung uu dai theo hang thanh vien.
- Quan ly menu rieng voi che do Goi mon.
- Quan ly khach hang va hang thanh vien.
- Thong ke doanh thu theo mon.

## 2. Cau truc du an

- src/quanlynhahang/gui: giao dien Swing.
- src/quanlynhahang/bus: xu ly nghiep vu.
- src/quanlynhahang/dao: truy cap du lieu, SQL, ket noi DB.
- src/quanlynhahang/dto: doi tuong truyen du lieu.
- src/quanlynhahang/dao/setupDatabase.sql: script tao CSDL, bang, view, procedure, trigger.
- lib: thu vien ngoai vi (JDBC driver).

## 3. Yeu cau he thong

- Java JDK 17 tro len.
- SQL Server (da bat TCP/IP).
- JDBC SQL Server driver trong thu muc lib.
- IDE: IntelliJ, Eclipse, hoac VS Code voi Java Extension Pack.

## 4. Huong dan setup

### Buoc 1: Lay ma nguon

```bash
git clone https://github.com/jimdabest/QuanLyNhaHang.git
cd QuanLyNhaHang
```

### Buoc 2: Khoi tao CSDL

1. Mo SQL Server Management Studio hoac DBeaver.
2. Chay file:

```text
src/quanlynhahang/dao/setupDatabase.sql
```

3. Dam bao script da tao day du:

- Database QuanLyNhaHang
- Bang, du lieu mau
- Views, Functions, Stored Procedures, Triggers

### Buoc 3: Cau hinh ket noi DB bang file .env

Du an su dung file .env tai thu muc goc de ket noi SQL Server.

Co the tao nhanh nhu sau:

```bash
copy .env.example .env
```

Noi dung mau .env:

```env
DB_SERVER=localhost
DB_PORT=1444
DB_DATABASE=QuanLyNhaHang
DB_USER=sa
DB_PASS=your_password_here
DB_ENCRYPT=true
DB_TRUST_SERVER_CERTIFICATE=true
```

Luu y:

- Khong commit file .env len git.
- Neu SQL Server cua ban dung cong 1433 thi doi DB_PORT=1433.

### Buoc 4: Cau hinh thu vien JDBC

- Dam bao trong thu muc lib co file mssql-jdbc*.jar.
- Neu IDE chua tu nhan thu vien, them file jar vao classpath/referenced libraries.

## 5. Build va chay

### Build nhanh bang PowerShell

```powershell
if (Test-Path out) { Remove-Item -Recurse -Force out }
New-Item -ItemType Directory -Path out | Out-Null
$sources = Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName }
& 'C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\javac.exe' -encoding UTF-8 -cp "lib/*" -d out $sources
```

### Chay ung dung

```powershell
& 'C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe' -cp "out;lib/*" quanlynhahang.gui.MainFrame
```
