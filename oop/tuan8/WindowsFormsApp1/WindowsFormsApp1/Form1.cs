using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace WindowsFormsApp1
{
    public partial class Form1 : Form
    {
        //thực hành 1
        //// 1. Chuỗi kết nối (sửa đường dẫn .mdf của bạn)
        //string strCon = @"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=QuanLyBanSach;Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        //// 2. Đối tượng kết nối tái sử dụng cho form
        //private SqlConnection sqlCon = null;

        //public Form1()
        //{
        //    InitializeComponent();
        //    this.Load += Form1_Load;
        //    lsvDanhSach.SelectedIndexChanged += lsvDanhSach_SelectedIndexChanged;
        //}

        //// ===== A. MỞ / ĐÓNG KẾT NỐI =====
        //private void MoKetNoi()
        //{
        //    if (sqlCon == null)
        //        sqlCon = new SqlConnection(strCon);
        //    if (sqlCon.State == ConnectionState.Closed)
        //        sqlCon.Open();
        //}

        //private void DongKetNoi()
        //{
        //    if (sqlCon != null && sqlCon.State == ConnectionState.Open)
        //        sqlCon.Close();
        //}

        //// ===== B. CẤU HÌNH LISTVIEW (nếu chưa cấu hình trong Designer) =====
        //private void InitListView()
        //{
        //    lsvDanhSach.View = View.Details;
        //    lsvDanhSach.FullRowSelect = true;
        //    lsvDanhSach.GridLines = true;

        //    if (lsvDanhSach.Columns.Count == 0)
        //    {
        //        lsvDanhSach.Columns.Add("Mã NXB", 100);
        //        lsvDanhSach.Columns.Add("Tên NXB", 220);
        //        lsvDanhSach.Columns.Add("Địa chỉ", 300);
        //    }
        //}

        //// ===== C. HIỂN THỊ DANH SÁCH NXB =====
        //private void HienThiDanhSachNXB()
        //{
        //    try
        //    {
        //        MoKetNoi();
        //        using (SqlCommand cmd = new SqlCommand("HienThiNXB", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            using (SqlDataReader reader = cmd.ExecuteReader())
        //            {
        //                lsvDanhSach.Items.Clear();
        //                while (reader.Read())
        //                {
        //                    string maNXB = reader.GetString(0).Trim();
        //                    string tenNXB = reader.GetString(1);
        //                    string diaChi = reader.IsDBNull(2) ? "" : reader.GetString(2);

        //                    ListViewItem lvi = new ListViewItem(maNXB);
        //                    lvi.SubItems.Add(tenNXB);
        //                    lvi.SubItems.Add(diaChi);
        //                    lsvDanhSach.Items.Add(lvi);
        //                }
        //            }
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi tải danh sách NXB: " + ex.Message);
        //    }
        //    finally
        //    {
        //        DongKetNoi();
        //    }
        //}

        //// ===== D. HIỂN THỊ CHI TIẾT THEO MÃ =====
        //private void HienThiThongTinNXBTheoMa(string maNXB)
        //{
        //    if (string.IsNullOrWhiteSpace(maNXB)) return;

        //    try
        //    {
        //        MoKetNoi();
        //        using (SqlCommand cmd = new SqlCommand("HienThiChiTietNXB", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            cmd.Parameters.Add(new SqlParameter("@MaNXB", SqlDbType.Char, 10) { Value = maNXB });

        //            using (SqlDataReader reader = cmd.ExecuteReader())
        //            {
        //                txtMaNXB.Text = txtTenNXB.Text = txtDiaChi.Text = "";
        //                if (reader.Read())
        //                {
        //                    txtMaNXB.Text = reader.GetString(0).Trim();
        //                    txtTenNXB.Text = reader.GetString(1);
        //                    txtDiaChi.Text = reader.IsDBNull(2) ? "" : reader.GetString(2);
        //                }
        //            }
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi tải chi tiết NXB: " + ex.Message);
        //    }
        //    finally
        //    {
        //        DongKetNoi();
        //    }
        //}

        //// ===== E. SỰ KIỆN =====
        //private void Form1_Load(object sender, EventArgs e)
        //{
        //    InitListView();
        //    HienThiDanhSachNXB();
        //}

        //private void lsvDanhSach_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //    if (lsvDanhSach.SelectedItems.Count == 0) return;
        //    string maNXB = lsvDanhSach.SelectedItems[0].SubItems[0].Text;
        //    HienThiThongTinNXBTheoMa(maNXB);
        //}


        //Thực hành 2
        // ==== Connection string ====
        // Sửa lại đường dẫn.mdf cho đúng máy bạn,
        // hoặc thay bằng chuỗi dùng Initial Catalog= QuanLySach nếu DB đã attach trên SQL Server.
        //string strCon = @"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=QuanLyBanSach;Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        //private SqlConnection sqlCon;

        //public Form1()
        //{
        //    InitializeComponent();

        //    // Gắn sự kiện ở code-behind để Designer gọn, tránh trùng
        //    this.Load += Form1_Load;
        //    lsvDanhSach.SelectedIndexChanged += lsvDanhSach_SelectedIndexChanged;
        //    btnThemDL.Click += btnThemDL_Click;
        //}

        //// ===== A. Mở / Đóng kết nối =====
        //private void MoKetNoi()
        //{
        //    if (sqlCon == null) sqlCon = new SqlConnection(strCon);
        //    if (sqlCon.State == ConnectionState.Closed) sqlCon.Open();
        //}

        //private void DongKetNoi()
        //{
        //    if (sqlCon != null && sqlCon.State == ConnectionState.Open)
        //        sqlCon.Close();
        //}

        //// ===== B. Hiển thị danh sách NXB =====
        //private void HienThiDanhSachNXB()
        //{
        //    try
        //    {
        //        MoKetNoi();
        //        using (var cmd = new SqlCommand("HienThiNXB", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            using (var reader = cmd.ExecuteReader())
        //            {
        //                lsvDanhSach.Items.Clear();
        //                while (reader.Read())
        //                {
        //                    string ma = reader.GetString(0).Trim();
        //                    string ten = reader.GetString(1);
        //                    string dia = reader.IsDBNull(2) ? "" : reader.GetString(2);

        //                    var lvi = new ListViewItem(ma);
        //                    lvi.SubItems.Add(ten);
        //                    lvi.SubItems.Add(dia);
        //                    lsvDanhSach.Items.Add(lvi);
        //                }
        //            }
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi tải danh sách: " + ex.Message);
        //    }
        //    finally { DongKetNoi(); }
        //}

        //// ===== C. Sự kiện Load =====
        //private void Form1_Load(object sender, EventArgs e)
        //{
        //    HienThiDanhSachNXB();
        //    txtMaNXB.Focus();
        //}

        //// ===== D. Chọn dòng để xem nhanh vào TextBox =====
        //private void lsvDanhSach_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //    if (lsvDanhSach.SelectedItems.Count == 0) return;
        //    var it = lsvDanhSach.SelectedItems[0];
        //    txtMaNXB.Text = it.SubItems[0].Text;
        //    txtTenNXB.Text = it.SubItems[1].Text;
        //    txtDiaChi.Text = it.SubItems[2].Text;
        //}

        //// ===== E. Nút Thêm nhà xuất bản =====
        //private void btnThemDL_Click(object sender, EventArgs e)
        //{
        //    string ma = (txtMaNXB.Text ?? "").Trim();
        //    string ten = (txtTenNXB.Text ?? "").Trim();
        //    string dia = (txtDiaChi.Text ?? "").Trim();

        //    if (string.IsNullOrEmpty(ma)) { MessageBox.Show("Vui lòng nhập Mã NXB."); txtMaNXB.Focus(); return; }
        //    if (ma.Length > 10) { MessageBox.Show("Mã NXB tối đa 10 ký tự."); txtMaNXB.Focus(); return; }
        //    if (string.IsNullOrEmpty(ten)) { MessageBox.Show("Vui lòng nhập Tên NXB."); txtTenNXB.Focus(); return; }

        //    try
        //    {
        //        MoKetNoi();
        //        using (var cmd = new SqlCommand("ThemNhaXuatBan", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            cmd.Parameters.Add(new SqlParameter("@NXB", SqlDbType.Char, 10) { Value = ma });
        //            cmd.Parameters.Add(new SqlParameter("@TenNXB", SqlDbType.NVarChar, 100) { Value = ten });
        //            cmd.Parameters.Add(new SqlParameter("@DiaChi", SqlDbType.NVarChar, 500) { Value = (object)dia ?? DBNull.Value });

        //            int kq = cmd.ExecuteNonQuery();
        //            if (kq > 0)
        //            {
        //                MessageBox.Show("Thêm dữ liệu thành công!");
        //                HienThiDanhSachNXB();
        //                txtMaNXB.Clear(); txtTenNXB.Clear(); txtDiaChi.Clear();
        //                txtMaNXB.Focus();
        //            }
        //            else
        //            {
        //                MessageBox.Show("Không có bản ghi nào được thêm.");
        //            }
        //        }
        //    }
        //    catch (SqlException ex) when (ex.Number == 2627 || ex.Number == 2601) // trùng khóa
        //    {
        //        MessageBox.Show("Mã NXB đã tồn tại. Vui lòng nhập mã khác.");
        //        txtMaNXB.Focus();
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi thêm dữ liệu: " + ex.Message);
        //    }
        //    finally { DongKetNoi(); }
        //}

        //Thực hành 3
        // === Connection string ===
        // Cách 1: LocalDB gắn file.mdf(đổi đường dẫn cho đúng)
        //string strCon = @"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=QuanLyBanSach;Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        //// Cách 2 (khuyên dùng nếu DB đã attach): dùng Initial Catalog (bỏ dòng trên, bỏ comment dòng dưới)
        //// private readonly string strCon =
        ////     @"Data Source=.\SQLEXPRESS;Initial Catalog=QuanLySach;Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        //private SqlConnection sqlCon;

        //public Form1()
        //{
        //    InitializeComponent();

        //    this.Load += Form1_Load;
        //    lsvDanhSach.SelectedIndexChanged += lsvDanhSach_SelectedIndexChanged;
        //    btnCapNhat.Click += btnCapNhat_Click;
        //}

        //// ===== A. Kết nối =====
        //private void MoKetNoi()
        //{
        //    if (sqlCon == null) sqlCon = new SqlConnection(strCon);
        //    if (sqlCon.State == ConnectionState.Closed) sqlCon.Open();
        //}
        //private void DongKetNoi()
        //{
        //    if (sqlCon != null && sqlCon.State == ConnectionState.Open)
        //        sqlCon.Close();
        //}

        //// ===== B. Hiển thị danh sách =====
        //private void HienThiDanhSachNXB()
        //{
        //    try
        //    {
        //        MoKetNoi();
        //        using (var cmd = new SqlCommand("HienThiNXB", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            using (var rd = cmd.ExecuteReader())
        //            {
        //                lsvDanhSach.BeginUpdate();
        //                lsvDanhSach.Items.Clear();
        //                while (rd.Read())
        //                {
        //                    string ma = rd.GetString(0).Trim();
        //                    string ten = rd.GetString(1);
        //                    string dia = rd.IsDBNull(2) ? "" : rd.GetString(2);

        //                    var it = new ListViewItem(ma);
        //                    it.SubItems.Add(ten);
        //                    it.SubItems.Add(dia);
        //                    lsvDanhSach.Items.Add(it);
        //                }
        //                lsvDanhSach.EndUpdate();
        //            }
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi tải danh sách: " + ex.Message);
        //    }
        //    finally { DongKetNoi(); }
        //}

        //// ===== C. Load form =====
        //private void Form1_Load(object sender, EventArgs e)
        //{
        //    HienThiDanhSachNXB();
        //    txtMaNXB.ReadOnly = true;   // cập nhật theo mã đang chọn
        //    btnCapNhat.Enabled = false; // chỉ bật khi chọn 1 dòng
        //}

        //// ===== D. Chọn 1 dòng để nạp vào ô nhập =====
        //private void lsvDanhSach_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //    if (lsvDanhSach.SelectedItems.Count == 0)
        //    {
        //        txtMaNXB.Clear(); txtTenNXB.Clear(); txtDiaChi.Clear();
        //        btnCapNhat.Enabled = false;
        //        return;
        //    }

        //    var it = lsvDanhSach.SelectedItems[0];
        //    txtMaNXB.Text = it.SubItems[0].Text;
        //    txtTenNXB.Text = it.SubItems[1].Text;
        //    txtDiaChi.Text = it.SubItems[2].Text;
        //    btnCapNhat.Enabled = true;
        //}

        //// ===== E. Nút CẬP NHẬT =====
        //private void btnCapNhat_Click(object sender, EventArgs e)
        //{
        //    string ma = (txtMaNXB.Text ?? "").Trim();
        //    string ten = (txtTenNXB.Text ?? "").Trim();
        //    string dia = (txtDiaChi.Text ?? "").Trim();

        //    if (string.IsNullOrEmpty(ma)) { MessageBox.Show("Chưa chọn bản ghi."); return; }
        //    if (string.IsNullOrEmpty(ten)) { MessageBox.Show("Vui lòng nhập Tên NXB."); txtTenNXB.Focus(); return; }

        //    try
        //    {
        //        MoKetNoi();
        //        using (var cmd = new SqlCommand("CapNhatNhaXuatBan", sqlCon))
        //        {
        //            cmd.CommandType = CommandType.StoredProcedure;
        //            cmd.Parameters.Add(new SqlParameter("@NXB", SqlDbType.Char, 10) { Value = ma });
        //            cmd.Parameters.Add(new SqlParameter("@TenNXB", SqlDbType.NVarChar, 100) { Value = ten });
        //            cmd.Parameters.Add(new SqlParameter("@DiaChi", SqlDbType.NVarChar, 500) { Value = (object)dia ?? DBNull.Value });

        //            int kq = cmd.ExecuteNonQuery();  // có thể trả -1 nếu NOCOUNT
        //            HienThiDanhSachNXB();            // luôn refresh sau khi cập nhật

        //            MessageBox.Show("Cập nhật xong (kết quả: " + kq + ").");
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        MessageBox.Show("Lỗi cập nhật: " + ex.Message);
        //    }
        //    finally { DongKetNoi(); }
        //}


        //Thực hành 4
        // ===== Connection string theo yêu cầu =====
        private readonly string strCon =
            @"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=QuanLyBanSach;Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        private SqlConnection sqlCon;

        public Form1()
        {
            InitializeComponent();

            this.Load += Form1_Load;
            lsvDanhSach.SelectedIndexChanged += lsvDanhSach_SelectedIndexChanged;
            btnXoa.Click += btnXoa_Click;
        }

        // ==== Kết nối ====
        private void MoKetNoi()
        {
            if (sqlCon == null) sqlCon = new SqlConnection(strCon);
            if (sqlCon.State == ConnectionState.Closed) sqlCon.Open();
        }
        private void DongKetNoi()
        {
            if (sqlCon != null && sqlCon.State == ConnectionState.Open)
                sqlCon.Close();
        }

        // ==== Hiển thị danh sách NXB ====
        private void HienThiDanhSachNXB()
        {
            try
            {
                MoKetNoi();
                using (var cmd = new SqlCommand("HienThiNXB", sqlCon))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    using (var rd = cmd.ExecuteReader())
                    {
                        lsvDanhSach.BeginUpdate();
                        lsvDanhSach.Items.Clear();
                        while (rd.Read())
                        {
                            string ma = rd.GetString(0).Trim();
                            string ten = rd.GetString(1);
                            string dia = rd.IsDBNull(2) ? "" : rd.GetString(2);

                            var it = new ListViewItem(ma);
                            it.SubItems.Add(ten);
                            it.SubItems.Add(dia);
                            lsvDanhSach.Items.Add(it);
                        }
                        lsvDanhSach.EndUpdate();
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi tải danh sách: " + ex.Message);
            }
            finally { DongKetNoi(); }
        }

        // ==== Form Load ====
        private void Form1_Load(object sender, EventArgs e)
        {
            HienThiDanhSachNXB();
            btnXoa.Enabled = false; // chỉ bật khi chọn 1 dòng
        }

        // ==== Chọn dòng ====
        private void lsvDanhSach_SelectedIndexChanged(object sender, EventArgs e)
        {
            btnXoa.Enabled = lsvDanhSach.SelectedItems.Count > 0;
        }

        // ==== Nút XÓA ====
        private void btnXoa_Click(object sender, EventArgs e)
        {
            if (lsvDanhSach.SelectedItems.Count == 0) return;

            var it = lsvDanhSach.SelectedItems[0];
            string ma = it.SubItems[0].Text;
            string ten = it.SubItems[1].Text;

            var confirm = MessageBox.Show(
                $"Bạn chắc chắn muốn xoá NXB '{ten}' (Mã: {ma})?",
                "Xác nhận xoá",
                MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm != DialogResult.Yes) return;

            try
            {
                MoKetNoi();
                using (var cmd = new SqlCommand("XoaNhaXuatBan", sqlCon))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.Parameters.Add(new SqlParameter("@NXB", SqlDbType.Char, 10) { Value = ma });

                    int kq = cmd.ExecuteNonQuery();   // có thể trả -1 nếu NOCOUNT
                    HienThiDanhSachNXB();             // luôn refresh
                    btnXoa.Enabled = false;

                    MessageBox.Show("Đã xử lý xoá (kết quả: " + kq + ").");
                }
            }
            catch (SqlException ex) when (ex.Number == 547) // FK constraint
            {
                MessageBox.Show("Không thể xoá vì NXB đang được tham chiếu bởi bảng khác (ví dụ: Sách).");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi xoá: " + ex.Message);
            }
            finally { DongKetNoi(); }
        }
    }
}
