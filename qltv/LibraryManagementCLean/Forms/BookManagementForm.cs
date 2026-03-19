using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms
{
    public class BookManagementForm : Form
    {
        private DataGridView dgvBooks;
        private TextBox txtSearch;
        private TextBox txtISBN;
        private TextBox txtTitle;
        private ComboBox cboCategory;
        private ComboBox cboAuthor;
        private ComboBox cboPublisher;
        private NumericUpDown numYear;
        private NumericUpDown numPrice;
        private NumericUpDown numTotal;
        private NumericUpDown numAvailable;
        private TextBox txtLocation;
        private TextBox txtDescription;
        private int selectedBookId;

        public BookManagementForm()
        {
            selectedBookId = 0;
            BuildUi();
            Load += BookManagementForm_Load;
        }

        private void BuildUi()
        {
            BackColor = Color.FromArgb(236, 239, 241);

            Label lblTitle = new Label();
            lblTitle.Text = "QUẢN LÝ SÁCH";
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(20, 15);

            txtSearch = new TextBox();
            txtSearch.PlaceholderText = "Tìm kiếm sách...";
            txtSearch.Location = new Point(20, 60);
            txtSearch.Width = 280;

            Button btnSearch = CreateButton("Tìm", 315, 58, Color.FromArgb(33, 136, 198));
            btnSearch.Click += BtnSearch_Click;

            Button btnRefresh = CreateButton("Làm mới", 410, 58, Color.FromArgb(33, 136, 198));
            btnRefresh.Click += BtnRefresh_Click;

            dgvBooks = new DataGridView();
            dgvBooks.Location = new Point(20, 105);
            dgvBooks.Size = new Size(820, 520);
            dgvBooks.ReadOnly = true;
            dgvBooks.AllowUserToAddRows = false;
            dgvBooks.AllowUserToDeleteRows = false;
            dgvBooks.BackgroundColor = Color.White;
            dgvBooks.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvBooks.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvBooks.RowHeadersVisible = false;
            dgvBooks.CellClick += DgvBooks_CellClick;

            Panel panelRight = new Panel();
            panelRight.BackColor = Color.White;
            panelRight.Location = new Point(860, 60);
            panelRight.Size = new Size(420, 565);

            int y = 15;
            panelRight.Controls.Add(MakeLabel("Thông tin sách", 18F, 18, ref y, true));

            txtISBN = AddText(panelRight, "ISBN", ref y);
            txtTitle = AddText(panelRight, "Tên sách", ref y);
            cboCategory = AddCombo(panelRight, "Thể loại", ref y);
            cboAuthor = AddCombo(panelRight, "Tác giả", ref y);
            cboPublisher = AddCombo(panelRight, "NXB", ref y);
            numYear = AddNumeric(panelRight, "Năm XB", ref y, 1900, 2100);
            numPrice = AddNumeric(panelRight, "Giá", ref y, 0, 10000000);
            numTotal = AddNumeric(panelRight, "Tổng số", ref y, 0, 10000);
            numAvailable = AddNumeric(panelRight, "Còn lại", ref y, 0, 10000);
            txtLocation = AddText(panelRight, "Vị trí", ref y);
            txtDescription = AddMulti(panelRight, "Mô tả", ref y);

            Button btnAdd = CreateButton("Thêm", 20, 520, Color.FromArgb(46, 204, 113));
            btnAdd.Click += BtnAdd_Click;
            Button btnUpdate = CreateButton("Sửa", 115, 520, Color.FromArgb(33, 136, 198));
            btnUpdate.Click += BtnUpdate_Click;
            Button btnDelete = CreateButton("Xóa", 210, 520, Color.FromArgb(231, 76, 60));
            btnDelete.Click += BtnDelete_Click;
            Button btnClear = CreateButton("Làm mới", 305, 520, Color.Gray);
            btnClear.Click += BtnClear_Click;

            panelRight.Controls.Add(btnAdd);
            panelRight.Controls.Add(btnUpdate);
            panelRight.Controls.Add(btnDelete);
            panelRight.Controls.Add(btnClear);

            Controls.Add(lblTitle);
            Controls.Add(txtSearch);
            Controls.Add(btnSearch);
            Controls.Add(btnRefresh);
            Controls.Add(dgvBooks);
            Controls.Add(panelRight);
        }

        private void BookManagementForm_Load(object sender, EventArgs e)
        {
            LoadLookups();
            LoadBooks(string.Empty);
        }

        private void LoadLookups()
        {
            cboCategory.DataSource = DatabaseHelper.ExecuteQuery("SELECT CategoryID, CategoryName FROM Categories WHERE IsActive = 1 ORDER BY CategoryName");
            cboCategory.DisplayMember = "CategoryName";
            cboCategory.ValueMember = "CategoryID";

            cboAuthor.DataSource = DatabaseHelper.ExecuteQuery("SELECT AuthorID, AuthorName FROM Authors WHERE IsActive = 1 ORDER BY AuthorName");
            cboAuthor.DisplayMember = "AuthorName";
            cboAuthor.ValueMember = "AuthorID";

            cboPublisher.DataSource = DatabaseHelper.ExecuteQuery("SELECT PublisherID, PublisherName FROM Publishers WHERE IsActive = 1 ORDER BY PublisherName");
            cboPublisher.DisplayMember = "PublisherName";
            cboPublisher.ValueMember = "PublisherID";
        }

        private void LoadBooks(string keyword)
        {
            dgvBooks.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT b.BookID, b.ISBN, b.Title AS [Tên sách], c.CategoryName AS [Thể loại], a.AuthorName AS [Tác giả],
                       b.TotalCopies AS [Tổng], b.AvailableCopies AS [Còn], b.Location AS [Vị trí]
                FROM Books b
                LEFT JOIN Categories c ON b.CategoryID = c.CategoryID
                LEFT JOIN Authors a ON b.AuthorID = a.AuthorID
                WHERE b.IsActive = 1
                  AND (@Keyword = N'' OR b.Title LIKE N'%' + @Keyword + N'%' OR b.ISBN LIKE N'%' + @Keyword + N'%')
                ORDER BY b.BookID DESC",
                new SqlParameter("@Keyword", keyword));
        }

        private void BtnAdd_Click(object sender, EventArgs e)
        {
            int result = DatabaseHelper.ExecuteNonQuery(@"
                INSERT INTO Books
                (ISBN, Title, CategoryID, AuthorID, PublisherID, PublishYear, Price, TotalCopies, AvailableCopies, Location, Description, IsActive, CreatedDate)
                VALUES
                (@ISBN, @Title, @CategoryID, @AuthorID, @PublisherID, @PublishYear, @Price, @TotalCopies, @AvailableCopies, @Location, @Description, 1, GETDATE())",
                new SqlParameter("@ISBN", txtISBN.Text.Trim()),
                new SqlParameter("@Title", txtTitle.Text.Trim()),
                new SqlParameter("@CategoryID", cboCategory.SelectedValue),
                new SqlParameter("@AuthorID", cboAuthor.SelectedValue),
                new SqlParameter("@PublisherID", cboPublisher.SelectedValue),
                new SqlParameter("@PublishYear", Convert.ToInt32(numYear.Value)),
                new SqlParameter("@Price", Convert.ToDecimal(numPrice.Value)),
                new SqlParameter("@TotalCopies", Convert.ToInt32(numTotal.Value)),
                new SqlParameter("@AvailableCopies", Convert.ToInt32(numAvailable.Value)),
                new SqlParameter("@Location", txtLocation.Text.Trim()),
                new SqlParameter("@Description", txtDescription.Text.Trim())
            );

            if (result > 0)
            {
                MessageBox.Show("Thêm sách thành công.");
                LoadBooks(string.Empty);
                ClearInput();
            }
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (selectedBookId == 0)
            {
                MessageBox.Show("Chọn sách trước.");
                return;
            }

            int result = DatabaseHelper.ExecuteNonQuery(@"
                UPDATE Books SET
                    ISBN=@ISBN, Title=@Title, CategoryID=@CategoryID, AuthorID=@AuthorID, PublisherID=@PublisherID,
                    PublishYear=@PublishYear, Price=@Price, TotalCopies=@TotalCopies, AvailableCopies=@AvailableCopies,
                    Location=@Location, Description=@Description, UpdatedDate=GETDATE()
                WHERE BookID=@BookID",
                new SqlParameter("@ISBN", txtISBN.Text.Trim()),
                new SqlParameter("@Title", txtTitle.Text.Trim()),
                new SqlParameter("@CategoryID", cboCategory.SelectedValue),
                new SqlParameter("@AuthorID", cboAuthor.SelectedValue),
                new SqlParameter("@PublisherID", cboPublisher.SelectedValue),
                new SqlParameter("@PublishYear", Convert.ToInt32(numYear.Value)),
                new SqlParameter("@Price", Convert.ToDecimal(numPrice.Value)),
                new SqlParameter("@TotalCopies", Convert.ToInt32(numTotal.Value)),
                new SqlParameter("@AvailableCopies", Convert.ToInt32(numAvailable.Value)),
                new SqlParameter("@Location", txtLocation.Text.Trim()),
                new SqlParameter("@Description", txtDescription.Text.Trim()),
                new SqlParameter("@BookID", selectedBookId)
            );

            if (result > 0)
            {
                MessageBox.Show("Cập nhật sách thành công.");
                LoadBooks(string.Empty);
                ClearInput();
            }
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            if (selectedBookId == 0)
            {
                MessageBox.Show("Chọn sách trước.");
                return;
            }

            int result = DatabaseHelper.ExecuteNonQuery(
                "UPDATE Books SET IsActive = 0, UpdatedDate = GETDATE() WHERE BookID = @BookID",
                new SqlParameter("@BookID", selectedBookId));

            if (result > 0)
            {
                MessageBox.Show("Xóa sách thành công.");
                LoadBooks(string.Empty);
                ClearInput();
            }
        }

        private void BtnSearch_Click(object sender, EventArgs e)
        {
            LoadBooks(txtSearch.Text.Trim());
        }

        private void BtnRefresh_Click(object sender, EventArgs e)
        {
            txtSearch.Clear();
            ClearInput();
            LoadBooks(string.Empty);
        }

        private void BtnClear_Click(object sender, EventArgs e)
        {
            ClearInput();
        }

        private void DgvBooks_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0 || dgvBooks.CurrentRow == null) return;

            selectedBookId = Convert.ToInt32(dgvBooks.CurrentRow.Cells["BookID"].Value);
            DataTable dt = DatabaseHelper.ExecuteQuery("SELECT * FROM Books WHERE BookID = @BookID",
                new SqlParameter("@BookID", selectedBookId));
            if (dt.Rows.Count == 0) return;

            DataRow row = dt.Rows[0];
            txtISBN.Text = row["ISBN"].ToString();
            txtTitle.Text = row["Title"].ToString();
            cboCategory.SelectedValue = row["CategoryID"];
            cboAuthor.SelectedValue = row["AuthorID"];
            cboPublisher.SelectedValue = row["PublisherID"];
            numYear.Value = Convert.ToDecimal(row["PublishYear"]);
            numPrice.Value = Convert.ToDecimal(row["Price"]);
            numTotal.Value = Convert.ToDecimal(row["TotalCopies"]);
            numAvailable.Value = Convert.ToDecimal(row["AvailableCopies"]);
            txtLocation.Text = row["Location"].ToString();
            txtDescription.Text = row["Description"].ToString();
        }

        private void ClearInput()
        {
            selectedBookId = 0;
            txtISBN.Clear();
            txtTitle.Clear();
            numYear.Value = 2000;
            numPrice.Value = 0;
            numTotal.Value = 1;
            numAvailable.Value = 1;
            txtLocation.Clear();
            txtDescription.Clear();
        }

        private Label MakeLabel(string text, float size, int x, ref int y, bool bold)
        {
            Label lbl = new Label();
            lbl.Text = text;
            lbl.AutoSize = true;
            lbl.Location = new Point(x, y);
            lbl.Font = new Font("Segoe UI", size, bold ? FontStyle.Bold : FontStyle.Regular);
            y += bold ? 40 : 26;
            return lbl;
        }

        private TextBox AddText(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            TextBox txt = new TextBox();
            txt.Location = new Point(18, y);
            txt.Width = 370;
            parent.Controls.Add(txt);
            y += 36;
            return txt;
        }

        private ComboBox AddCombo(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            ComboBox cbo = new ComboBox();
            cbo.Location = new Point(18, y);
            cbo.Width = 370;
            cbo.DropDownStyle = ComboBoxStyle.DropDownList;
            parent.Controls.Add(cbo);
            y += 36;
            return cbo;
        }

        private NumericUpDown AddNumeric(Control parent, string caption, ref int y, int min, int max)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            NumericUpDown num = new NumericUpDown();
            num.Location = new Point(18, y);
            num.Width = 370;
            num.Minimum = min;
            num.Maximum = max;
            num.Value = min < 1 ? 0 : 1;
            parent.Controls.Add(num);
            y += 36;
            return num;
        }

        private TextBox AddMulti(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            TextBox txt = new TextBox();
            txt.Location = new Point(18, y);
            txt.Width = 370;
            txt.Height = 70;
            txt.Multiline = true;
            txt.ScrollBars = ScrollBars.Vertical;
            parent.Controls.Add(txt);
            y += 85;
            return txt;
        }

        private Button CreateButton(string text, int x, int y, Color color)
        {
            Button btn = new Button();
            btn.Text = text;
            btn.Location = new Point(x, y);
            btn.Size = new Size(82, 34);
            btn.BackColor = color;
            btn.ForeColor = Color.White;
            btn.FlatStyle = FlatStyle.Flat;
            btn.FlatAppearance.BorderSize = 0;
            return btn;
        }
    }
}

