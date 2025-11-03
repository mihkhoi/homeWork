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

namespace th3
{
    public partial class Form1 : Form
    {
        private readonly string strCon = @"Data Source=(localdb)\MSSQLLocalDB;
Initial Catalog=QuanLyBanSach;
Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        private SqlConnection sqlCon;

        public Form1()
        {
            InitializeComponent();

            dgvDanhSach.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvDanhSach.ReadOnly = true;
            dgvDanhSach.AllowUserToAddRows = false;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try { HienThiDuLieu(); }
            catch (Exception ex) { MessageBox.Show("Lỗi tải dữ liệu: " + ex.Message); }
        }

        private void MoKetNoi()
        {
            if (sqlCon == null) sqlCon = new SqlConnection(strCon);
            if (sqlCon.State == ConnectionState.Closed) sqlCon.Open();
        }
        private void DongKetNoi()
        {
            if (sqlCon != null && sqlCon.State == ConnectionState.Open) sqlCon.Close();
        }

        private void HienThiDuLieu()
        {
            MoKetNoi();
            using (var da = new SqlDataAdapter(
                "SELECT MaNXB, TenNXB, DiaChi, DienThoai FROM dbo.NhaXuatBan", sqlCon))
            {
                var dt = new DataTable();
                da.Fill(dt);
                dgvDanhSach.DataSource = dt;
            }
            DongKetNoi();
        }

        private void btnHienThi_Click(object sender, EventArgs e) => HienThiDuLieu();

        private void dgvDanhSach_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0) return; // click tiêu đề cột
            var row = dgvDanhSach.Rows[e.RowIndex];
            txtMaXB.Text = Convert.ToString(row.Cells["MaNXB"].Value);
            txtTenXB.Text = Convert.ToString(row.Cells["TenNXB"].Value);
            txtDiaChi.Text = Convert.ToString(row.Cells["DiaChi"].Value);
            txtDienThoai.Text = Convert.ToString(row.Cells["DienThoai"].Value);
        }

        private void btnCapNhat_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtMaXB.Text))
            {
                MessageBox.Show("Hãy chọn 1 dòng trong bảng trước khi cập nhật!");
                return;
            }
            if (string.IsNullOrWhiteSpace(txtTenXB.Text))
            {
                MessageBox.Show("Tên nhà xuất bản không được để trống!");
                txtTenXB.Focus();
                return;
            }

            try
            {
                MoKetNoi();

                string sql = @"UPDATE dbo.NhaXuatBan
                               SET TenNXB = @Ten, DiaChi = @DiaChi, DienThoai = @DienThoai
                               WHERE MaNXB = @Ma";

                using (var cmd = new SqlCommand(sql, sqlCon))
                {
                    cmd.Parameters.Add("@Ma", SqlDbType.Int).Value = int.Parse(txtMaXB.Text.Trim());
                    var pTen = cmd.Parameters.Add("@Ten", SqlDbType.NVarChar, 200);
                    pTen.Value = txtTenXB.Text.Trim();

                    var pDiaChi = cmd.Parameters.Add("@DiaChi", SqlDbType.NVarChar, 300);
                    pDiaChi.Value = string.IsNullOrWhiteSpace(txtDiaChi.Text) ? (object)DBNull.Value : txtDiaChi.Text.Trim();

                    var pDT = cmd.Parameters.Add("@DienThoai", SqlDbType.NVarChar, 50);
                    pDT.Value = string.IsNullOrWhiteSpace(txtDienThoai.Text) ? (object)DBNull.Value : txtDienThoai.Text.Trim();

                    int kq = cmd.ExecuteNonQuery();
                    if (kq > 0)
                    {
                        MessageBox.Show("Cập nhật thành công!");
                        HienThiDuLieu();
                        XoaForm();
                    }
                    else
                    {
                        MessageBox.Show("Không có bản ghi nào được cập nhật!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi cập nhật: " + ex.Message);
            }
            finally
            {
                DongKetNoi();
            }
        }

        private void btnClear_Click(object sender, EventArgs e) => XoaForm();

        private void XoaForm()
        {
            txtMaXB.Text = "";
            txtTenXB.Text = "";
            txtDiaChi.Text = "";
            txtDienThoai.Text = "";
            txtTenXB.Focus();
        }
    }
}
