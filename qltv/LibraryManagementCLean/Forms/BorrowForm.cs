using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms
{
    public class BorrowForm : Form
    {
        private TextBox txtMemberCode;
        private Label lblMemberInfo;
        private TextBox txtBookSearch;
        private DataGridView dgvBooks;
        private DataGridView dgvCurrentBorrow;
        private NumericUpDown numDays;
        private int currentMemberId;

        public BorrowForm()
        {
            currentMemberId = 0;
            BuildUi();
            Load += BorrowForm_Load;
        }

        private void BuildUi()
        {
            BackColor = Color.FromArgb(236, 239, 241);

            Label lblTitle = new Label();
            lblTitle.Text = "MƯỢN SÁCH";
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(20, 15);

            Panel panelMember = new Panel();
            panelMember.BackColor = Color.White;
            panelMember.Location = new Point(20, 60);
            panelMember.Size = new Size(430, 250);

            Label lbl1 = new Label();
            lbl1.Text = "Thông tin độc giả";
            lbl1.Font = new Font("Segoe UI", 14F, FontStyle.Bold);
            lbl1.AutoSize = true;
            lbl1.Location = new Point(18, 16);

            Label lbl2 = new Label();
            lbl2.Text = "Mã thẻ:";
            lbl2.AutoSize = true;
            lbl2.Location = new Point(18, 58);

            txtMemberCode = new TextBox();
            txtMemberCode.Location = new Point(80, 54);
            txtMemberCode.Width = 150;

            Button btnFind = new Button();
            btnFind.Text = "Tìm kiếm";
            btnFind.Location = new Point(245, 52);
            btnFind.Size = new Size(100, 34);
            btnFind.BackColor = Color.FromArgb(33, 136, 198);
            btnFind.ForeColor = Color.White;
            btnFind.FlatStyle = FlatStyle.Flat;
            btnFind.FlatAppearance.BorderSize = 0;
            btnFind.Click += BtnFind_Click;

            lblMemberInfo = new Label();
            lblMemberInfo.Text = "Chưa chọn độc giả";
            lblMemberInfo.AutoSize = false;
            lblMemberInfo.Size = new Size(380, 130);
            lblMemberInfo.Font = new Font("Segoe UI", 11F);
            lblMemberInfo.Location = new Point(18, 105);

            panelMember.Controls.Add(lbl1);
            panelMember.Controls.Add(lbl2);
            panelMember.Controls.Add(txtMemberCode);
            panelMember.Controls.Add(btnFind);
            panelMember.Controls.Add(lblMemberInfo);

            Label lbl3 = new Label();
            lbl3.Text = "Sách đang mượn:";
            lbl3.Font = new Font("Segoe UI", 14F, FontStyle.Bold);
            lbl3.AutoSize = true;
            lbl3.Location = new Point(20, 330);

            dgvCurrentBorrow = new DataGridView();
            dgvCurrentBorrow.Location = new Point(20, 368);
            dgvCurrentBorrow.Size = new Size(430, 250);
            dgvCurrentBorrow.ReadOnly = true;
            dgvCurrentBorrow.AllowUserToAddRows = false;
            dgvCurrentBorrow.AllowUserToDeleteRows = false;
            dgvCurrentBorrow.BackgroundColor = Color.White;
            dgvCurrentBorrow.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvCurrentBorrow.RowHeadersVisible = false;

            Panel panelBook = new Panel();
            panelBook.BackColor = Color.White;
            panelBook.Location = new Point(470, 60);
            panelBook.Size = new Size(710, 500);

            Label lbl4 = new Label();
            lbl4.Text = "Chọn sách mượn";
            lbl4.Font = new Font("Segoe UI", 14F, FontStyle.Bold);
            lbl4.AutoSize = true;
            lbl4.Location = new Point(18, 16);

            txtBookSearch = new TextBox();
            txtBookSearch.PlaceholderText = "Nhập tên sách hoặc ISBN...";
            txtBookSearch.Location = new Point(18, 50);
            txtBookSearch.Width = 300;
            txtBookSearch.TextChanged += TxtBookSearch_TextChanged;

            dgvBooks = new DataGridView();
            dgvBooks.Location = new Point(18, 88);
            dgvBooks.Size = new Size(670, 330);
            dgvBooks.ReadOnly = true;
            dgvBooks.AllowUserToAddRows = false;
            dgvBooks.AllowUserToDeleteRows = false;
            dgvBooks.BackgroundColor = Color.White;
            dgvBooks.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvBooks.RowHeadersVisible = false;

            Label lbl5 = new Label();
            lbl5.Text = "Số ngày mượn:";
            lbl5.AutoSize = true;
            lbl5.Location = new Point(18, 440);

            numDays = new NumericUpDown();
            numDays.Location = new Point(120, 436);
            numDays.Width = 80;
            numDays.Minimum = 1;
            numDays.Maximum = 30;
            numDays.Value = 7;

            Button btnBorrow = new Button();
            btnBorrow.Text = "Mượn sách";
            btnBorrow.Location = new Point(220, 432);
            btnBorrow.Size = new Size(130, 36);
            btnBorrow.BackColor = Color.FromArgb(46, 204, 113);
            btnBorrow.ForeColor = Color.White;
            btnBorrow.FlatStyle = FlatStyle.Flat;
            btnBorrow.FlatAppearance.BorderSize = 0;
            btnBorrow.Click += BtnBorrow_Click;

            panelBook.Controls.Add(lbl4);
            panelBook.Controls.Add(txtBookSearch);
            panelBook.Controls.Add(dgvBooks);
            panelBook.Controls.Add(lbl5);
            panelBook.Controls.Add(numDays);
            panelBook.Controls.Add(btnBorrow);

            Controls.Add(lblTitle);
            Controls.Add(panelMember);
            Controls.Add(lbl3);
            Controls.Add(dgvCurrentBorrow);
            Controls.Add(panelBook);
        }

        private void BorrowForm_Load(object sender, EventArgs e)
        {
            LoadBooks();
        }

        private void BtnFind_Click(object sender, EventArgs e)
        {
            DataTable dt = DatabaseHelper.ExecuteQuery(
                "SELECT TOP 1 * FROM Members WHERE MemberCode = @Code AND IsActive = 1",
                new SqlParameter("@Code", txtMemberCode.Text.Trim()));

            if (dt.Rows.Count == 0)
            {
                currentMemberId = 0;
                lblMemberInfo.Text = "Không tìm thấy độc giả.";
                dgvCurrentBorrow.DataSource = null;
                return;
            }

            DataRow row = dt.Rows[0];
            currentMemberId = Convert.ToInt32(row["MemberID"]);
            lblMemberInfo.Text = "Họ tên: " + row["FullName"].ToString() + "\r\n"
                               + "Loại thẻ: " + row["MemberType"].ToString() + "\r\n"
                               + "Điện thoại: " + row["Phone"].ToString() + "\r\n"
                               + "Hạn thẻ: " + Convert.ToDateTime(row["ExpiryDate"]).ToString("dd/MM/yyyy");

            LoadCurrentBorrow();
        }

        private void LoadBooks()
        {
            dgvBooks.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT BookID, ISBN, Title AS [Tên sách], AvailableCopies AS [Còn lại], Location AS [Vị trí]
                FROM Books
                WHERE IsActive = 1 AND AvailableCopies > 0
                  AND (@Keyword = N'' OR Title LIKE N'%' + @Keyword + N'%' OR ISBN LIKE N'%' + @Keyword + N'%')
                ORDER BY Title",
                new SqlParameter("@Keyword", txtBookSearch.Text.Trim()));
        }

        private void LoadCurrentBorrow()
        {
            if (currentMemberId == 0)
            {
                dgvCurrentBorrow.DataSource = null;
                return;
            }

            dgvCurrentBorrow.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT b.Title AS [Tên sách], br.BorrowDate AS [Ngày], br.DueDate AS [Hạn trả], br.Status AS [Trạng thái]
                FROM BorrowRecords br
                INNER JOIN Books b ON br.BookID = b.BookID
                WHERE br.MemberID = @MemberID AND br.Status = N'Đang mượn'
                ORDER BY br.BorrowID DESC",
                new SqlParameter("@MemberID", currentMemberId));
        }

        private void TxtBookSearch_TextChanged(object sender, EventArgs e)
        {
            LoadBooks();
        }

        private void BtnBorrow_Click(object sender, EventArgs e)
        {
            if (currentMemberId == 0)
            {
                MessageBox.Show("Hãy tìm độc giả trước.");
                return;
            }

            if (dgvBooks.CurrentRow == null)
            {
                MessageBox.Show("Hãy chọn sách.");
                return;
            }

            int bookId = Convert.ToInt32(dgvBooks.CurrentRow.Cells["BookID"].Value);
            DateTime borrowDate = DateTime.Today;
            DateTime dueDate = borrowDate.AddDays(Convert.ToInt32(numDays.Value));

            using (SqlConnection conn = DatabaseHelper.GetConnection())
            {
                conn.Open();
                using (SqlTransaction tran = conn.BeginTransaction())
                {
                    try
                    {
                        int available;
                        using (SqlCommand cmdCheck = new SqlCommand("SELECT AvailableCopies FROM Books WHERE BookID = @BookID", conn, tran))
                        {
                            cmdCheck.Parameters.AddWithValue("@BookID", bookId);
                            available = Convert.ToInt32(cmdCheck.ExecuteScalar());
                        }

                        if (available <= 0)
                        {
                            tran.Rollback();
                            MessageBox.Show("Sách đã hết.");
                            return;
                        }

                        string borrowCode = "PM" + DateTime.Now.ToString("yyyyMMddHHmmss");

                        using (SqlCommand cmdInsert = new SqlCommand(@"
                            INSERT INTO BorrowRecords
                            (BorrowCode, MemberID, BookID, BorrowDate, DueDate, Quantity, Status, FineAmount, ProcessedBy, CreatedDate)
                            VALUES
                            (@BorrowCode, @MemberID, @BookID, @BorrowDate, @DueDate, 1, N'Đang mượn', 0, N'Quản lý', GETDATE())", conn, tran))
                        {
                            cmdInsert.Parameters.AddWithValue("@BorrowCode", borrowCode);
                            cmdInsert.Parameters.AddWithValue("@MemberID", currentMemberId);
                            cmdInsert.Parameters.AddWithValue("@BookID", bookId);
                            cmdInsert.Parameters.AddWithValue("@BorrowDate", borrowDate);
                            cmdInsert.Parameters.AddWithValue("@DueDate", dueDate);
                            cmdInsert.ExecuteNonQuery();
                        }

                        using (SqlCommand cmdUpdate = new SqlCommand("UPDATE Books SET AvailableCopies = AvailableCopies - 1 WHERE BookID = @BookID", conn, tran))
                        {
                            cmdUpdate.Parameters.AddWithValue("@BookID", bookId);
                            cmdUpdate.ExecuteNonQuery();
                        }

                        tran.Commit();
                        MessageBox.Show("Mượn sách thành công.");
                    }
                    catch (Exception ex)
                    {
                        tran.Rollback();
                        MessageBox.Show("Lỗi mượn sách: " + ex.Message);
                    }
                }
            }

            LoadBooks();
            LoadCurrentBorrow();
        }
    }
}

