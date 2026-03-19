USE master;
GO

IF EXISTS (SELECT 1 FROM sys.databases WHERE name = N'LibraryManagementCLean')
BEGIN
    ALTER DATABASE LibraryManagementCLean SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE LibraryManagementCLean;
END
GO

CREATE DATABASE LibraryManagementCLean;
GO
USE LibraryManagementCLean;
GO

CREATE TABLE Users
(
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL UNIQUE,
    [Password] NVARCHAR(100) NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    RoleName NVARCHAR(50) NOT NULL DEFAULT N'Manager',
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE Categories
(
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL UNIQUE,
    IsActive BIT NOT NULL DEFAULT 1
);
GO

CREATE TABLE Authors
(
    AuthorID INT IDENTITY(1,1) PRIMARY KEY,
    AuthorName NVARCHAR(150) NOT NULL,
    IsActive BIT NOT NULL DEFAULT 1
);
GO

CREATE TABLE Publishers
(
    PublisherID INT IDENTITY(1,1) PRIMARY KEY,
    PublisherName NVARCHAR(150) NOT NULL,
    IsActive BIT NOT NULL DEFAULT 1
);
GO

CREATE TABLE Books
(
    BookID INT IDENTITY(1,1) PRIMARY KEY,
    ISBN NVARCHAR(30) NOT NULL UNIQUE,
    Title NVARCHAR(300) NOT NULL,
    CategoryID INT NOT NULL,
    AuthorID INT NOT NULL,
    PublisherID INT NOT NULL,
    PublishYear INT NOT NULL DEFAULT 2000,
    Price DECIMAL(18,0) NOT NULL DEFAULT 0,
    TotalCopies INT NOT NULL DEFAULT 1,
    AvailableCopies INT NOT NULL DEFAULT 1,
    Location NVARCHAR(100) NULL,
    Description NVARCHAR(MAX) NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    UpdatedDate DATETIME NULL,
    CONSTRAINT FK_Books_Categories FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID),
    CONSTRAINT FK_Books_Authors FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID),
    CONSTRAINT FK_Books_Publishers FOREIGN KEY (PublisherID) REFERENCES Publishers(PublisherID)
);
GO

CREATE TABLE Members
(
    MemberID INT IDENTITY(1,1) PRIMARY KEY,
    MemberCode NVARCHAR(20) NOT NULL UNIQUE,
    FullName NVARCHAR(150) NOT NULL,
    Gender NVARCHAR(10) NULL,
    BirthDate DATE NULL,
    Phone NVARCHAR(20) NULL,
    Email NVARCHAR(100) NULL,
    IdentityNumber NVARCHAR(20) NULL,
    Address NVARCHAR(255) NULL,
    MemberType NVARCHAR(50) NULL,
    JoinDate DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    ExpiryDate DATE NULL,
    FineDebt DECIMAL(18,0) NOT NULL DEFAULT 0,
    Note NVARCHAR(255) NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    UpdatedDate DATETIME NULL
);
GO

CREATE TABLE BorrowRecords
(
    BorrowID INT IDENTITY(1,1) PRIMARY KEY,
    BorrowCode NVARCHAR(30) NOT NULL UNIQUE,
    MemberID INT NOT NULL,
    BookID INT NOT NULL,
    BorrowDate DATE NOT NULL,
    DueDate DATE NOT NULL,
    ReturnDate DATE NULL,
    Quantity INT NOT NULL DEFAULT 1,
    Status NVARCHAR(30) NOT NULL DEFAULT N'Đang mượn',
    FineAmount DECIMAL(18,0) NOT NULL DEFAULT 0,
    ProcessedBy NVARCHAR(100) NULL,
    CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
    UpdatedDate DATETIME NULL,
    CONSTRAINT FK_Borrow_Members FOREIGN KEY (MemberID) REFERENCES Members(MemberID),
    CONSTRAINT FK_Borrow_Books FOREIGN KEY (BookID) REFERENCES Books(BookID)
);
GO

INSERT INTO Users (Username, [Password], FullName, RoleName)
VALUES (N'admin', N'123456', N'Quản lý', N'Manager');
GO

INSERT INTO Categories (CategoryName) VALUES
(N'Công nghệ thông tin'),
(N'Khoa học'),
(N'Kinh tế'),
(N'Kỹ năng sống'),
(N'Lịch sử'),
(N'Nghệ thuật'),
(N'Ngoại ngữ'),
(N'Thiếu nhi'),
(N'Văn học'),
(N'Y học');
GO

INSERT INTO Authors (AuthorName) VALUES
(N'Robert T. Kiyosaki'),
(N'Nam Cao'),
(N'Nguyễn Nhật Ánh'),
(N'Dale Carnegie'),
(N'Tô Hoài'),
(N'Stephen Hawking'),
(N'Paulo Coelho'),
(N'Yuval Noah Harari');
GO

INSERT INTO Publishers (PublisherName) VALUES
(N'NXB Trẻ'),
(N'NXB Giáo dục'),
(N'NXB Lao động'),
(N'NXB Tổng hợp'),
(N'NXB Kim Đồng');
GO

INSERT INTO Books (ISBN, Title, CategoryID, AuthorID, PublisherID, PublishYear, Price, TotalCopies, AvailableCopies, Location, Description)
VALUES
(N'978-604-1-12345-7', N'Cha giàu cha nghèo', 3, 1, 1, 2019, 125000, 5, 5, N'B1-01', N'Sách kinh điển về tư duy tài chính cá nhân.'),
(N'978-604-1-12345-8', N'Chí Phèo', 9, 2, 2, 2018, 65000, 4, 4, N'A1-03', N'Tác phẩm tiêu biểu của Nam Cao.'),
(N'978-604-1-12345-9', N'Cho tôi xin một vé đi tuổi thơ', 9, 3, 1, 2020, 85000, 5, 5, N'A1-01', N'Tác phẩm nổi tiếng của Nguyễn Nhật Ánh.'),
(N'978-604-1-12346-0', N'Đắc nhân tâm', 4, 4, 3, 2017, 99000, 6, 6, N'B2-01', N'Sách kỹ năng sống nổi tiếng.'),
(N'978-604-1-12346-1', N'Dế Mèn phiêu lưu ký', 8, 5, 5, 2016, 72000, 3, 3, N'A1-02', N'Tác phẩm thiếu nhi kinh điển.'),
(N'978-604-1-12346-2', N'Lược sử thời gian', 2, 6, 4, 2015, 110000, 2, 2, N'C2-01', N'Sách khoa học nổi tiếng.'),
(N'978-604-1-12346-3', N'Nhà giả kim', 9, 7, 4, 2014, 89000, 4, 4, N'A2-01', N'Tiểu thuyết truyền cảm hứng.'),
(N'978-604-1-12346-4', N'Sapiens: Lược sử loài người', 2, 8, 4, 2021, 165000, 3, 3, N'C1-01', N'Tác phẩm nổi tiếng về lịch sử loài người.');
GO

INSERT INTO Members (MemberCode, FullName, Gender, BirthDate, Phone, Email, IdentityNumber, Address, MemberType, ExpiryDate, FineDebt, Note)
VALUES
(N'TV001', N'Nguyễn Văn An', N'Nam', '2001-03-08', N'0901234567', N'an@email.com', N'079123456789', N'Hà Nội', N'Thường', DATEADD(YEAR, 1, GETDATE()), 0, N''),
(N'TV002', N'Trần Thị Bình', N'Nữ', '2002-06-15', N'0912345678', N'binh@email.com', N'079223456789', N'TP.HCM', N'VIP', DATEADD(YEAR, 1, GETDATE()), 0, N''),
(N'TV003', N'Lê Văn Cường', N'Nam', '2000-09-11', N'0923456789', N'cuong@email.com', N'079323456789', N'Đà Nẵng', N'Sinh viên', DATEADD(YEAR, 1, GETDATE()), 0, N''),
(N'TV004', N'Phạm Thị Dung', N'Nữ', '1999-12-01', N'0934567890', N'dung@email.com', N'079423456789', N'Huế', N'Giáo viên', DATEADD(YEAR, 1, GETDATE()), 0, N''),
(N'TV005', N'Hoàng Văn Em', N'Nam', '2002-03-08', N'0945678901', N'em@email.com', N'079523456789', N'TP.HCM', N'Sinh viên', DATEADD(YEAR, 1, GETDATE()), 0, N'');
GO
