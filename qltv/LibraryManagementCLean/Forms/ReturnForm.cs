using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms
{
    public class ReturnForm : Form
    {
        private TextBox txtSearch;
        private ComboBox cboStatus;
        private DataGridView dgvRecords;
        private int selectedBorrowId;
        private int selectedBookId;
        private DateTime selectedDueDate;
        private string selectedStatus;

        public ReturnForm()
        {
            selectedBorrowId = 0;
            selectedBookId = 0;
            selectedDueDate = DateTime.Today;
            selectedStatus = string.Empty;
            BuildUi();
            Load += ReturnForm_Load;
        }

        private void BuildUi()
        {
            BackColor = Color.FromArgb(236, 239, 241);

            Label lblTitle = new Label();
            lblTitle.Text = "TRẢ SÁCH";
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(20, 15);

            Panel panelTop = new Panel();
            panelTop.BackColor = Color.White;
            panelTop.Location = new Point(20, 60);
            panelTop.Size = new Size(1020, 58);

            txtSearch = new TextBox();
            txtSearch.PlaceholderText = "Tìm kiếm: mã phiếu, mã thẻ, tên độc giả...";
            txtSearch.Location = new Point(16, 14);
            txtSearch.Width = 300;

            cboStatus = new ComboBox();
            cboStatus.Location = new Point(335, 14);
            cboStatus.Width = 170;
            cboStatus.DropDownStyle = ComboBoxStyle.DropDownList;
            cboStatus.Items.AddRange(new object[] { "-- Tất cả --", "Đang mượn", "Đã trả" });
            cboStatus.SelectedIndex = 0;

            Button btnLoad = new Button();
            btnLoad.Text = "Làm mới";
            btnLoad.Location = new Point(525, 12);
            btnLoad.Size = new Size(100, 34);
            btnLoad.BackColor = Color.FromArgb(33, 136, 198);
            btnLoad.ForeColor = Color.White;
            btnLoad.FlatStyle = FlatStyle.Flat;
            btnLoad.FlatAppearance.BorderSize = 0;
            btnLoad.Click += BtnLoad_Click;

            panelTop.Controls.Add(txtSearch);
            panelTop.Controls.Add(cboStatus);
            panelTop.Controls.Add(btnLoad);

            dgvRecords = new DataGridView();
            dgvRecords.Location = new Point(20, 135);
            dgvRecords.Size = new Size(1020, 420);
            dgvRecords.ReadOnly = true;
            dgvRecords.AllowUserToAddRows = false;
            dgvRecords.AllowUserToDeleteRows = false;
            dgvRecords.BackgroundColor = Color.White;
            dgvRecords.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvRecords.RowHeadersVisible = false;
            dgvRecords.CellClick += DgvRecords_CellClick;

            Button btnReturn = new Button();
            btnReturn.Text = "Trả sách";
            btnReturn.Location = new Point(720, 575);
            btnReturn.Size = new Size(120, 36);
            btnReturn.BackColor = Color.FromArgb(46, 204, 113);
            btnReturn.ForeColor = Color.White;
            btnReturn.FlatStyle = FlatStyle.Flat;
            btnReturn.FlatAppearance.BorderSize = 0;
            btnReturn.Click += BtnReturn_Click;

            Button btnRenew = new Button();
            btnRenew.Text = "Gia hạn";
            btnRenew.Location = new Point(855, 575);
            btnRenew.Size = new Size(120, 36);
            btnRenew.BackColor = Color.FromArgb(33, 136, 198);
            btnRenew.ForeColor = Color.White;
            btnRenew.FlatStyle = FlatStyle.Flat;
            btnRenew.FlatAppearance.BorderSize = 0;
            btnRenew.Click += BtnRenew_Click;

            Controls.Add(lblTitle);
            Controls.Add(panelTop);
            Controls.Add(dgvRecords);
            Controls.Add(btnReturn);
            Controls.Add(btnRenew);
        }

        private void ReturnForm_Load(object sender, EventArgs e)
        {
            LoadRecords();
        }

        private void LoadRecords()
        {
            string status = cboStatus.SelectedIndex <= 0 ? string.Empty : cboStatus.Text;

            dgvRecords.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT br.BorrowID, br.BookID, br.BorrowCode AS [Mã phiếu], m.MemberCode AS [Mã thẻ],
                       m.FullName AS [Tên độc giả], b.Title AS [Tên sách], br.BorrowDate AS [Ngày mượn],
                       br.DueDate AS [Hạn trả], br.Status AS [Trạng thái]
                FROM BorrowRecords br
                INNER JOIN Members m ON br.MemberID = m.MemberID
                INNER JOIN Books b ON br.BookID = b.BookID
                WHERE (@Keyword = N'' OR br.BorrowCode LIKE N'%' + @Keyword + N'%' OR m.MemberCode LIKE N'%' + @Keyword + N'%' OR m.FullName LIKE N'%' + @Keyword + N'%')
                  AND (@Status = N'' OR br.Status = @Status)
                ORDER BY br.BorrowID DESC",
                new SqlParameter("@Keyword", txtSearch.Text.Trim()),
                new SqlParameter("@Status", status));
        }

        private void BtnLoad_Click(object sender, EventArgs e)
        {
            LoadRecords();
        }

        private void DgvRecords_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0 || dgvRecords.CurrentRow == null) return;

            selectedBorrowId = Convert.ToInt32(dgvRecords.CurrentRow.Cells["BorrowID"].Value);
            selectedBookId = Convert.ToInt32(dgvRecords.CurrentRow.Cells["BookID"].Value);
            selectedDueDate = Convert.ToDateTime(dgvRecords.CurrentRow.Cells["Hạn trả"].Value);
            selectedStatus = dgvRecords.CurrentRow.Cells["Trạng thái"].Value.ToString();
        }

        private void BtnReturn_Click(object sender, EventArgs e)
        {
            if (selectedBorrowId == 0)
            {
                MessageBox.Show("Chọn phiếu mượn trước.");
                return;
            }

            if (selectedStatus == "Đã trả")
            {
                MessageBox.Show("Phiếu này đã được trả.");
                return;
            }

            decimal fineAmount = 0;
            int overdueDays = (DateTime.Today - selectedDueDate.Date).Days;
            if (overdueDays > 0)
            {
                fineAmount = overdueDays * 1000m;
            }

            using (SqlConnection conn = DatabaseHelper.GetConnection())
            {
                conn.Open();
                using (SqlTransaction tran = conn.BeginTransaction())
                {
                    try
                    {
                        using (SqlCommand cmd1 = new SqlCommand(@"
                            UPDATE BorrowRecords
                            SET ReturnDate = @ReturnDate, Status = N'Đã trả', FineAmount = @FineAmount
                            WHERE BorrowID = @BorrowID", conn, tran))
                        {
                            cmd1.Parameters.AddWithValue("@ReturnDate", DateTime.Today);
                            cmd1.Parameters.AddWithValue("@FineAmount", fineAmount);
                            cmd1.Parameters.AddWithValue("@BorrowID", selectedBorrowId);
                            cmd1.ExecuteNonQuery();
                        }

                        using (SqlCommand cmd2 = new SqlCommand("UPDATE Books SET AvailableCopies = AvailableCopies + 1 WHERE BookID = @BookID", conn, tran))
                        {
                            cmd2.Parameters.AddWithValue("@BookID", selectedBookId);
                            cmd2.ExecuteNonQuery();
                        }

                        tran.Commit();
                        MessageBox.Show("Trả sách thành công. Tiền phạt: " + string.Format("{0:N0}", fineAmount) + " đ");
                    }
                    catch (Exception ex)
                    {
                        tran.Rollback();
                        MessageBox.Show("Lỗi trả sách: " + ex.Message);
                    }
                }
            }

            LoadRecords();
        }

        private void BtnRenew_Click(object sender, EventArgs e)
        {
            if (selectedBorrowId == 0)
            {
                MessageBox.Show("Chọn phiếu mượn trước.");
                return;
            }

            if (selectedStatus != "Đang mượn")
            {
                MessageBox.Show("Chỉ gia hạn được phiếu đang mượn.");
                return;
            }

            int result = DatabaseHelper.ExecuteNonQuery(
                "UPDATE BorrowRecords SET DueDate = DATEADD(DAY, 7, DueDate) WHERE BorrowID = @BorrowID",
                new SqlParameter("@BorrowID", selectedBorrowId));

            if (result > 0)
            {
                MessageBox.Show("Gia hạn thêm 7 ngày thành công.");
                LoadRecords();
            }
        }
    }
}

