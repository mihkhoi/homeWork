//using System;
//using System.Collections.Generic;
//using System.ComponentModel;
//using System.Data;
//using System.Data.SqlClient;
//using System.Drawing;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;
//using System.Windows.Forms;

//namespace QLSV_NoParam
//{
//    public partial class Form1 : Form
//    {
//        private readonly string strCon =
//    @"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=D:\Code\oop\tuan7\QLSV_NoParam\QLSV_NoParam\QuanLySinhVien.mdf;Integrated Security=True;Connect Timeout=30";
// // 2) Kết nối cấp Form
//        private SqlConnection sqlCon = null;

//        public Form1()
//        {
//            InitializeComponent();
//        }

//        // ==== A. MỞ / ĐÓNG KẾT NỐI ====
//        private void MoKetNoi()
//        {
//            if (sqlCon == null)
//                sqlCon = new SqlConnection(strCon);
//            if (sqlCon.State == ConnectionState.Closed)
//                sqlCon.Open();
//        }
//        private void DongKetNoi()
//        {
//            if (sqlCon != null && sqlCon.State == ConnectionState.Open)
//                sqlCon.Close();
//        }

//        // ==== B. HIỂN THỊ LISTVIEW ====
//        private void HienThiDanhSach()
//        {
//            try
//            {
//                MoKetNoi();

//                using (var cmd = new SqlCommand(
//                    "SELECT MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop FROM SinhVien ORDER BY MaSV", sqlCon))
//                using (var rd = cmd.ExecuteReader())
//                {
//                    lsvDanhSachSV.Items.Clear();
//                    while (rd.Read())
//                    {
//                        var item = new ListViewItem(rd.GetString(0));                // MaSV
//                        item.SubItems.Add(rd.GetString(1));                           // TenSV
//                        item.SubItems.Add(rd.GetString(2));                           // GioiTinh
//                        item.SubItems.Add(rd.GetDateTime(3).ToString("dd/MM/yyyy"));  // NgaySinh
//                        item.SubItems.Add(rd.IsDBNull(4) ? "" : rd.GetString(4));     // QueQuan
//                        item.SubItems.Add(rd.GetString(5));                           // MaLop
//                        lsvDanhSachSV.Items.Add(item);
//                    }
//                }
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi hiển thị: " + ex.Message);
//            }
//            finally
//            {
//                DongKetNoi();
//            }
//        }

//        // ==== C. THÊM SINH VIÊN (KHÔNG DÙNG PARAMETER) ====
//        private void btnThemSinhVien_Click(object sender, EventArgs e)
//        {
//            string maSV = txtMaSV.Text.Trim();
//            string tenSV = txtTenSV.Text.Trim();
//            string gioiTinh = cbGioiTinh.SelectedItem?.ToString() ?? "";  // KHÔNG dùng SelectedText
//            string ngaySinh = dtpNgaySinh.Value.ToString("MM/dd/yyyy");   // để SQL hiểu kiểu Date
//            string queQuan = txtQueQuan.Text.Trim();
//            string maLop = txtMaLop.Text.Trim();

//            // Kiểm tra cơ bản
//            if (string.IsNullOrWhiteSpace(maSV) ||
//                string.IsNullOrWhiteSpace(tenSV) ||
//                string.IsNullOrWhiteSpace(gioiTinh) ||
//                string.IsNullOrWhiteSpace(maLop))
//            {
//                MessageBox.Show("Nhập đủ Mã SV, Tên SV, Giới tính, Mã lớp!");
//                return;
//            }

//            try
//            {
//                MoKetNoi();

//                // GHÉP CHUỖI (theo yêu cầu đề) + Escape dấu nháy đơn
//                string sql = "INSERT INTO SinhVien VALUES (" +
//                             "'" + maSV.Replace("'", "''") + "', " +
//                             "N'" + tenSV.Replace("'", "''") + "', " +
//                             "N'" + gioiTinh.Replace("'", "''") + "', " +
//                             "'" + ngaySinh + "', " +
//                             "N'" + queQuan.Replace("'", "''") + "', " +
//                             "'" + maLop.Replace("'", "''") + "')";

//                using (var cmd = new SqlCommand(sql, sqlCon))
//                {
//                    int kq = cmd.ExecuteNonQuery();   // <<< yêu cầu bài: ExecuteNonQuery
//                    if (kq > 0)
//                    {
//                        MessageBox.Show("Thêm sinh viên thành công!");
//                        HienThiDanhSach();
//                        ClearInputs();
//                    }
//                    else MessageBox.Show("Không có bản ghi nào được thêm.");
//                }
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi thêm dữ liệu: " + ex.Message);
//            }
//            finally
//            {
//                DongKetNoi();
//            }
//        }

//        private void ClearInputs()
//        {
//            txtMaSV.Clear();
//            txtTenSV.Clear();
//            cbGioiTinh.SelectedIndex = -1;
//            dtpNgaySinh.Value = DateTime.Today;
//            txtQueQuan.Clear();
//            txtMaLop.Clear();
//            txtMaSV.Focus();
//        }

//        // ==== D. FORM LOAD ====
//        private void Form1_Load(object sender, EventArgs e)
//        {
//            // Nạp giới tính
//            cbGioiTinh.Items.Clear();
//            cbGioiTinh.Items.Add("Nam");
//            cbGioiTinh.Items.Add("Nữ");

//            // Nếu chưa thêm cột trong Designer, đảm bảo cột tồn tại
//            if (lsvDanhSachSV.Columns.Count == 0)
//            {
//                lsvDanhSachSV.Columns.Add("Mã SV", 90);
//                lsvDanhSachSV.Columns.Add("Tên SV", 160);
//                lsvDanhSachSV.Columns.Add("Giới tính", 70);
//                lsvDanhSachSV.Columns.Add("Ngày sinh", 90);
//                lsvDanhSachSV.Columns.Add("Quê quán", 150);
//                lsvDanhSachSV.Columns.Add("Mã lớp", 80);
//            }

//            HienThiDanhSach();
//        }
//    }
//}
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

//namespace QLSV_NoParam
//{
//    public partial class Form1 : Form
//    {
//        // Giữ nguyên chuỗi kết nối tuyệt đối của bạn
//        private readonly string strCon =
//            @"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=D:\Code\oop\tuan7\QLSV_NoParam\QLSV_NoParam\QuanLySinhVien.mdf;Integrated Security=True;Connect Timeout=30";

//        // 2) Kết nối cấp Form
//        private SqlConnection sqlCon = null;

//        public Form1()
//        {
//            InitializeComponent();
//        }

//        // ==== A. MỞ / ĐÓNG KẾT NỐI ====
//        private void MoKetNoi()
//        {
//            if (sqlCon == null)
//                sqlCon = new SqlConnection(strCon);
//            if (sqlCon.State == ConnectionState.Closed)
//                sqlCon.Open();
//        }
//        private void DongKetNoi()
//        {
//            if (sqlCon != null && sqlCon.State == ConnectionState.Open)
//                sqlCon.Close();
//        }

//        // ==== B. HIỂN THỊ LISTVIEW ====
//        private void HienThiDanhSach()
//        {
//            try
//            {
//                MoKetNoi();

//                using (var cmd = new SqlCommand(
//                    "SELECT MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop FROM dbo.SinhVien ORDER BY MaSV", sqlCon))
//                using (var rd = cmd.ExecuteReader())
//                {
//                    lsvDanhSachSV.Items.Clear();
//                    while (rd.Read())
//                    {
//                        var item = new ListViewItem(rd.GetString(0));                // MaSV
//                        item.SubItems.Add(rd.GetString(1));                           // TenSV
//                        item.SubItems.Add(rd.GetString(2));                           // GioiTinh
//                        item.SubItems.Add(rd.GetDateTime(3).ToString("dd/MM/yyyy"));  // NgaySinh
//                        item.SubItems.Add(rd.IsDBNull(4) ? "" : rd.GetString(4));     // QueQuan
//                        item.SubItems.Add(rd.GetString(5));                           // MaLop
//                        lsvDanhSachSV.Items.Add(item);
//                    }
//                }
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi hiển thị: " + ex.Message);
//            }
//            finally
//            {
//                DongKetNoi();
//            }
//        }

//        // =======================
//        //   THÊM BẰNG PARAMETER
//        // =======================
//        private int InsertSinhVien(string maSV, string tenSV, string gioiTinh,
//                                   DateTime ngaySinh, string queQuan, string maLop)
//        {
//            const string SQL = @"
//                INSERT INTO dbo.SinhVien(MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop)
//                VALUES(@MaSV, @TenSV, @GioiTinh, @NgaySinh, @QueQuan, @MaLop);";

//            MoKetNoi();
//            using (var cmd = new SqlCommand(SQL, sqlCon))
//            {
//                // Khai báo đúng SqlDbType + length
//                cmd.Parameters.Add("@MaSV", SqlDbType.NVarChar, 20).Value = maSV;
//                cmd.Parameters.Add("@TenSV", SqlDbType.NVarChar, 100).Value = tenSV;
//                cmd.Parameters.Add("@GioiTinh", SqlDbType.NVarChar, 5).Value = gioiTinh;
//                cmd.Parameters.Add("@NgaySinh", SqlDbType.Date).Value = ngaySinh.Date;

//                if (string.IsNullOrWhiteSpace(queQuan))
//                    cmd.Parameters.Add("@QueQuan", SqlDbType.NVarChar, 100).Value = DBNull.Value;
//                else
//                    cmd.Parameters.Add("@QueQuan", SqlDbType.NVarChar, 100).Value = queQuan;

//                cmd.Parameters.Add("@MaLop", SqlDbType.NVarChar, 20).Value = maLop;

//                return cmd.ExecuteNonQuery();
//            }
//        }

//        // ==== C. NÚT THÊM SINH VIÊN (GỌI HÀM PARAMETER) ====
//        private void btnThemSinhVien_Click(object sender, EventArgs e)
//        {
//            string maSV = txtMaSV.Text.Trim();
//            string tenSV = txtTenSV.Text.Trim();
//            string gioiTinh = cbGioiTinh.SelectedItem?.ToString() ?? "";  // KHÔNG dùng SelectedText
//            DateTime ngaySinh = dtpNgaySinh.Value;
//            string queQuan = txtQueQuan.Text.Trim();
//            string maLop = txtMaLop.Text.Trim();

//            // Kiểm tra cơ bản
//            if (string.IsNullOrWhiteSpace(maSV) ||
//                string.IsNullOrWhiteSpace(tenSV) ||
//                string.IsNullOrWhiteSpace(gioiTinh) ||
//                string.IsNullOrWhiteSpace(maLop))
//            {
//                MessageBox.Show("Nhập đủ Mã SV, Tên SV, Giới tính, Mã lớp!");
//                return;
//            }

//            try
//            {
//                int kq = InsertSinhVien(maSV, tenSV, gioiTinh, ngaySinh, queQuan, maLop);
//                if (kq > 0)
//                {
//                    MessageBox.Show("Thêm sinh viên (parameter) thành công!");
//                    HienThiDanhSach();
//                    ClearInputs();
//                }
//                else
//                {
//                    MessageBox.Show("Không có bản ghi nào được thêm.");
//                }
//            }
//            catch (SqlException ex) when (ex.Number == 2627 || ex.Number == 2601) // Trùng khóa (MaSV)
//            {
//                MessageBox.Show("Mã sinh viên đã tồn tại!");
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi thêm dữ liệu: " + ex.Message);
//            }
//            finally
//            {
//                DongKetNoi();
//            }
//        }

//        private void ClearInputs()
//        {
//            txtMaSV.Clear();
//            txtTenSV.Clear();
//            cbGioiTinh.SelectedIndex = -1;
//            dtpNgaySinh.Value = DateTime.Today;
//            txtQueQuan.Clear();
//            txtMaLop.Clear();
//            txtMaSV.Focus();
//        }

//        // ==== D. FORM LOAD ====
//        private void Form1_Load(object sender, EventArgs e)
//        {
//            // Nạp giới tính
//            cbGioiTinh.Items.Clear();
//            cbGioiTinh.Items.Add("Nam");
//            cbGioiTinh.Items.Add("Nữ");

//            // Nếu chưa thêm cột trong Designer, đảm bảo cột tồn tại
//            if (lsvDanhSachSV.Columns.Count == 0)
//            {
//                lsvDanhSachSV.Columns.Add("Mã SV", 90);
//                lsvDanhSachSV.Columns.Add("Tên SV", 160);
//                lsvDanhSachSV.Columns.Add("Giới tính", 70);
//                lsvDanhSachSV.Columns.Add("Ngày sinh", 90);
//                lsvDanhSachSV.Columns.Add("Quê quán", 150);
//                lsvDanhSachSV.Columns.Add("Mã lớp", 80);
//            }

//            HienThiDanhSach();
//        }
//    }
//}

//namespace QLSV_NoParam
//{
//    public partial class Form1 : Form
//    {
//        // Dùng chung chuỗi kết nối như Form1 (đổi theo của bạn)
//        private readonly string strCon =
//            @"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=D:\Code\oop\tuan7\QLSV_NoParam\QLSV_NoParam\QuanLySinhVien.mdf;Integrated Security=True;Connect Timeout=30";

//        private SqlConnection con;

//        public Form1()
//        {
//            InitializeComponent();
//        }

//        // ===== Helpers =====
//        private void OpenConn()
//        {
//            if (con == null) con = new SqlConnection(strCon);
//            if (con.State == ConnectionState.Closed) con.Open();
//        }
//        private void CloseConn()
//        {
//            if (con != null && con.State == ConnectionState.Open) con.Close();
//        }

//        private void FrmSuaSinhVien_Load(object sender, EventArgs e)
//        {
//            cbGioiTinh.Items.Clear();
//            cbGioiTinh.Items.Add("Nam");
//            cbGioiTinh.Items.Add("Nữ");

//            // Thêm cột cho ListView nếu cần
//            if (lsvSV.Columns.Count == 0)
//            {
//                lsvSV.Columns.Add("Mã SV", 90);
//                lsvSV.Columns.Add("Tên SV", 160);
//                lsvSV.Columns.Add("Giới tính", 70);
//                lsvSV.Columns.Add("Ngày sinh", 90);
//                lsvSV.Columns.Add("Quê quán", 150);
//                lsvSV.Columns.Add("Mã lớp", 80);
//            }

//            LoadLops();
//        }

//        private void LoadLops()
//        {
//            try
//            {
//                OpenConn();
//                using (var cmd = new SqlCommand("SELECT DISTINCT MaLop FROM dbo.SinhVien ORDER BY MaLop", con))
//                using (var rd = cmd.ExecuteReader())
//                {
//                    cboLop.Items.Clear();
//                    while (rd.Read())
//                        cboLop.Items.Add(rd.GetString(0));
//                }
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi nạp mã lớp: " + ex.Message);
//            }
//            finally { CloseConn(); }
//        }

//        private void cboLop_SelectedIndexChanged(object sender, EventArgs e)
//        {
//            LoadStudentsByClass(cboLop.SelectedItem?.ToString());
//        }

//        private void LoadStudentsByClass(string maLop)
//        {
//            if (string.IsNullOrEmpty(maLop)) return;

//            try
//            {
//                OpenConn();
//                using (var cmd = new SqlCommand(
//                    @"SELECT MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop 
//                      FROM dbo.SinhVien 
//                      WHERE MaLop = @MaLop 
//                      ORDER BY MaSV", con))
//                {
//                    cmd.Parameters.Add("@MaLop", SqlDbType.NVarChar, 20).Value = maLop;
//                    using (var rd = cmd.ExecuteReader())
//                    {
//                        lsvSV.Items.Clear();
//                        while (rd.Read())
//                        {
//                            var it = new ListViewItem(rd.GetString(0));
//                            it.SubItems.Add(rd.GetString(1));
//                            it.SubItems.Add(rd.GetString(2));
//                            it.SubItems.Add(rd.GetDateTime(3).ToString("dd/MM/yyyy"));
//                            it.SubItems.Add(rd.IsDBNull(4) ? "" : rd.GetString(4));
//                            it.SubItems.Add(rd.GetString(5));
//                            lsvSV.Items.Add(it);
//                        }
//                    }
//                }
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi nạp sinh viên: " + ex.Message);
//            }
//            finally { CloseConn(); }
//        }

//        private void lsvSV_SelectedIndexChanged(object sender, EventArgs e)
//        {
//            if (lsvSV.SelectedItems.Count == 0) return;
//            var it = lsvSV.SelectedItems[0];
//            txtMaSV.Text = it.SubItems[0].Text;
//            txtTenSV.Text = it.SubItems[1].Text;
//            cbGioiTinh.SelectedItem = it.SubItems[2].Text;
//            // parse ngày
//            if (DateTime.TryParse(it.SubItems[3].Text, out var d))
//                dtpNgaySinh.Value = d;
//            else
//                dtpNgaySinh.Value = DateTime.Today;
//            txtQueQuan.Text = it.SubItems[4].Text;
//            txtMaLop.Text = it.SubItems[5].Text;
//        }

//        // ====== SỬA bằng Parameter ======
//        private int UpdateSinhVien(string maSV, string tenSV, string gioiTinh,
//                                   DateTime ngaySinh, string queQuan, string maLop)
//        {
//            const string SQL = @"
//                UPDATE dbo.SinhVien
//                SET TenSV   = @TenSV,
//                    GioiTinh= @GioiTinh,
//                    NgaySinh= @NgaySinh,
//                    QueQuan = @QueQuan,
//                    MaLop   = @MaLop
//                WHERE MaSV   = @MaSV;";

//            OpenConn();
//            using (var cmd = new SqlCommand(SQL, con))
//            {
//                cmd.Parameters.Add("@TenSV", SqlDbType.NVarChar, 100).Value = tenSV;
//                cmd.Parameters.Add("@GioiTinh", SqlDbType.NVarChar, 5).Value = gioiTinh;
//                cmd.Parameters.Add("@NgaySinh", SqlDbType.Date).Value = ngaySinh.Date;
//                if (string.IsNullOrWhiteSpace(queQuan))
//                    cmd.Parameters.Add("@QueQuan", SqlDbType.NVarChar, 100).Value = DBNull.Value;
//                else
//                    cmd.Parameters.Add("@QueQuan", SqlDbType.NVarChar, 100).Value = queQuan;
//                cmd.Parameters.Add("@MaLop", SqlDbType.NVarChar, 20).Value = maLop;
//                cmd.Parameters.Add("@MaSV", SqlDbType.NVarChar, 20).Value = maSV;

//                return cmd.ExecuteNonQuery();
//            }
//        }

//        private void btnSua_Click(object sender, EventArgs e)
//        {
//            if (string.IsNullOrWhiteSpace(txtMaSV.Text))
//            {
//                MessageBox.Show("Chọn một sinh viên ở danh sách bên trái.");
//                return;
//            }

//            try
//            {
//                int kq = UpdateSinhVien(
//                    txtMaSV.Text.Trim(),
//                    txtTenSV.Text.Trim(),
//                    cbGioiTinh.SelectedItem?.ToString() ?? "",
//                    dtpNgaySinh.Value,
//                    txtQueQuan.Text.Trim(),
//                    txtMaLop.Text.Trim()
//                );

//                if (kq > 0)
//                {
//                    MessageBox.Show("Đã cập nhật!");
//                    // refresh theo lớp đang chọn
//                    var lop = cboLop.SelectedItem?.ToString();
//                    if (!string.IsNullOrEmpty(lop)) LoadStudentsByClass(lop);
//                }
//                else MessageBox.Show("Không có bản ghi nào được cập nhật.");
//            }
//            catch (Exception ex)
//            {
//                MessageBox.Show("Lỗi sửa dữ liệu: " + ex.Message);
//            }
//            finally { CloseConn(); }
//        }

//        // ====== (THAM KHẢO) SỬA KHÔNG DÙNG PARAMETER ======
//        // Nếu bài yêu cầu phiên bản không Parameter, dùng hàm này thay cho UpdateSinhVien:
//        /*
//        private int UpdateSinhVien_NoParam(string maSV, string tenSV, string gioiTinh,
//                                           DateTime ngaySinh, string queQuan, string maLop)
//        {
//            string sql = "UPDATE dbo.SinhVien SET " +
//                         "TenSV=N'" + tenSV.Replace("'", "''") + "', " +
//                         "GioiTinh=N'" + gioiTinh.Replace("'", "''") + "', " +
//                         "NgaySinh='" + ngaySinh.ToString("MM/dd/yyyy") + "', " +
//                         "QueQuan=N'" + (queQuan ?? "").Replace("'", "''") + "', " +
//                         "MaLop='" + maLop.Replace("'", "''") + "' " +
//                         "WHERE MaSV='" + maSV.Replace("'", "''") + "'";
//            OpenConn();
//            using (var cmd = new SqlCommand(sql, con))
//                return cmd.ExecuteNonQuery();
//        }
//        */
//    }
//}
namespace QLSV_NoParam
{
    public partial class Form1 : Form
    {
        private readonly string strCon =
            @"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=D:\Code\oop\tuan7\QLSV_NoParam\QLSV_NoParam\QuanLySinhVien.mdf;Integrated Security=True;Connect Timeout=30";
        private SqlConnection con;

        public Form1()
        {
            InitializeComponent();
        }

        private void OpenConn()
        {
            if (con == null) con = new SqlConnection(strCon);
            if (con.State == ConnectionState.Closed) con.Open();
        }
        private void CloseConn()
        {
            if (con != null && con.State == ConnectionState.Open) con.Close();
        }

        private void FrmXoaSinhVien_Load(object sender, EventArgs e)
        {
            // đảm bảo cột đã có nếu tạo form trống
            if (lsvSV.Columns.Count == 0)
            {
                lsvSV.Columns.Add("Mã SV", 90);
                lsvSV.Columns.Add("Tên SV", 160);
                lsvSV.Columns.Add("Giới tính", 70);
                lsvSV.Columns.Add("Ngày sinh", 95);
                lsvSV.Columns.Add("Quê quán", 160);
                lsvSV.Columns.Add("Mã lớp", 80);
            }
            LoadStudents();
        }

        private void LoadStudents()
        {
            try
            {
                OpenConn();
                using (var cmd = new SqlCommand(
                    "SELECT MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop FROM dbo.SinhVien ORDER BY MaSV", con))
                using (var rd = cmd.ExecuteReader())
                {
                    lsvSV.Items.Clear();
                    while (rd.Read())
                    {
                        var it = new ListViewItem(rd.GetString(0));
                        it.SubItems.Add(rd.GetString(1));
                        it.SubItems.Add(rd.GetString(2));
                        it.SubItems.Add(rd.GetDateTime(3).ToString("dd/MM/yyyy"));
                        it.SubItems.Add(rd.IsDBNull(4) ? "" : rd.GetString(4));
                        it.SubItems.Add(rd.GetString(5));
                        lsvSV.Items.Add(it);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi tải danh sách: " + ex.Message);
            }
            finally { CloseConn(); }
        }

        private int DeleteSinhVien(string maSV)
        {
            const string SQL = "DELETE FROM dbo.SinhVien WHERE MaSV = @MaSV";
            OpenConn();
            using (var cmd = new SqlCommand(SQL, con))
            {
                cmd.Parameters.Add("@MaSV", SqlDbType.NVarChar, 20).Value = maSV;
                return cmd.ExecuteNonQuery();
            }
        }

        private void btnXoa_Click(object sender, EventArgs e)
        {
            if (lsvSV.SelectedItems.Count == 0)
            {
                MessageBox.Show("Chọn một sinh viên trong danh sách để xóa.");
                return;
            }
            var maSV = lsvSV.SelectedItems[0].SubItems[0].Text;

            if (MessageBox.Show($"Bạn chắc muốn xóa sinh viên {maSV}?",
                                "Xác nhận", MessageBoxButtons.YesNo, MessageBoxIcon.Warning)
                != DialogResult.Yes) return;

            try
            {
                int kq = DeleteSinhVien(maSV);
                if (kq > 0)
                {
                    MessageBox.Show("Đã xóa!");
                    LoadStudents();
                }
                else MessageBox.Show("Không có bản ghi nào bị xóa.");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi xóa dữ liệu: " + ex.Message);
            }
            finally { CloseConn(); }
        }
    }
}
