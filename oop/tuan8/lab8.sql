USE QuanLyBanSach;
GO

-- Dùng lại để hiển thị
CREATE OR ALTER PROCEDURE dbo.HienThiNXB
AS
BEGIN
    SET NOCOUNT ON;
    SELECT NXB, TenNXB, DiaChi
    FROM dbo.NhaXuatBan
    ORDER BY TenNXB;
END
GO

-- ✅ Thủ tục XÓA
CREATE OR ALTER PROCEDURE dbo.XoaNhaXuatBan
    @NXB CHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    -- Nếu đang bị ràng buộc ở bảng khác (vd: Sách) thì báo rõ
    IF EXISTS (SELECT 1 FROM sys.objects WHERE name = 'Sach' AND type = 'U')
       AND EXISTS (SELECT 1 FROM dbo.Sach WHERE NXB = @NXB)
    BEGIN
        RAISERROR (N'NXB đang được tham chiếu bởi Sách. Không thể xóa.', 16, 1);
        RETURN;
    END

    IF NOT EXISTS (SELECT 1 FROM dbo.NhaXuatBan WHERE NXB = @NXB)
    BEGIN
        RAISERROR (N'Không tìm thấy mã NXB để xóa.', 16, 1);
        RETURN;
    END

    DELETE FROM dbo.NhaXuatBan WHERE NXB = @NXB;
END
GO
