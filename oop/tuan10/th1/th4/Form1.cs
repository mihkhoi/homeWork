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

namespace th4
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
            if (e.RowIndex < 0) return; // header
            var row = dgvDanhSach.Rows[e.RowIndex];
            txtMaXB.Text = Convert.ToString(row.Cells["MaNXB"].Value);
            txtTenXB.Text = Convert.ToString(row.Cells["TenNXB"].Value);
            txtDiaChi.Text = Convert.ToString(row.Cells["DiaChi"].Value);
            txtDienThoai.Text = Convert.ToString(row.Cells["DienThoai"].Value);
        }

        private void btnXoa_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtMaXB.Text))
            {
                MessageBox.Show("Bạn chưa chọn dòng nào để xóa!");
                return;
            }

            // Hỏi xác nhận
            var confirm = MessageBox.Show(
                "Bạn có chắc chắn muốn xóa bản ghi này?",
                "Cảnh báo", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm != DialogResult.Yes) return;

            try
            {
                MoKetNoi();

                string sql = "DELETE FROM dbo.NhaXuatBan WHERE MaNXB = @Ma";
                using (var cmd = new SqlCommand(sql, sqlCon))
                {
                    cmd.Parameters.Add("@Ma", SqlDbType.Int).Value = int.Parse(txtMaXB.Text.Trim());
                    int kq = cmd.ExecuteNonQuery();

                    if (kq > 0)
                    {
                        MessageBox.Show("Xóa dữ liệu thành công!");
                        HienThiDuLieu();
                        XoaForm();
                    }
                    else
                    {
                        MessageBox.Show("Không có bản ghi nào bị xóa!");
                    }
                }
            }
            catch (SqlException ex)
            {
                // Nếu có khóa ngoại ở bảng Sách tham chiếu NXB thì ở đây sẽ ném lỗi
                MessageBox.Show("Không thể xóa do ràng buộc dữ liệu: " + ex.Message);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi xóa: " + ex.Message);
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
        }
    }
}
