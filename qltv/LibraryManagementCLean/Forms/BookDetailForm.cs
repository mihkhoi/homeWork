using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms {
public
class BookDetailForm : Form {
  private
    readonly int _bookId;

  private
    Label lblTitle;
  private
    Label lblInfo;
  private
    TextBox txtDescription;
  private
    Label lblStock;
  private
    Label lblQty;

  public
    BookDetailForm(int bookId) {
        _bookId = bookId;
        BuildUi();
        Load += BookDetailForm_Load;
    }

  private
    void BuildUi() {
        Text = "Chi tiết sách";
        StartPosition = FormStartPosition.CenterParent;
        FormBorderStyle = FormBorderStyle.FixedDialog;
        MaximizeBox = false;
        MinimizeBox = false;
        ClientSize = new Size(760, 520);
        BackColor = Color.FromArgb(236, 239, 241);

        Panel panelLeft = new Panel();
        panelLeft.BackColor = Color.White;
        panelLeft.Location = new Point(18, 18);
        panelLeft.Size = new Size(180, 420);

        Panel cover = new Panel();
        cover.BackColor = Color.FromArgb(245, 245, 245);
        cover.BorderStyle = BorderStyle.FixedSingle;
        cover.Location = new Point(20, 20);
        cover.Size = new Size(140, 220);

        Label lblIcon = new Label();
        lblIcon.Text = "📖";
        lblIcon.Font = new Font("Segoe UI Emoji", 44F);
        lblIcon.AutoSize = true;
        lblIcon.Location = new Point(35, 75);
        cover.Controls.Add(lblIcon);

        lblStock = new Label();
        lblStock.Font = new Font("Segoe UI", 15F, FontStyle.Bold);
        lblStock.AutoSize = true;
        lblStock.Location = new Point(20, 265);

        lblQty = new Label();
        lblQty.Font = new Font("Segoe UI", 11F);
        lblQty.AutoSize = true;
        lblQty.Location = new Point(20, 305);

        panelLeft.Controls.Add(cover);
        panelLeft.Controls.Add(lblStock);
        panelLeft.Controls.Add(lblQty);

        Panel panelRight = new Panel();
        panelRight.BackColor = Color.White;
        panelRight.Location = new Point(215, 18);
        panelRight.Size = new Size(520, 420);

        lblTitle = new Label();
        lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
        lblTitle.AutoSize = true;
        lblTitle.Location = new Point(20, 20);

        lblInfo = new Label();
        lblInfo.Font = new Font("Segoe UI", 11F);
        lblInfo.AutoSize = false;
        lblInfo.Size = new Size(470, 180);
        lblInfo.Location = new Point(20, 80);

        Label lblDesc = new Label();
        lblDesc.Text = "Mô tả:";
        lblDesc.Font = new Font("Segoe UI", 12F, FontStyle.Bold);
        lblDesc.AutoSize = true;
        lblDesc.Location = new Point(20, 265);

        txtDescription = new TextBox();
        txtDescription.Location = new Point(20, 295);
        txtDescription.Size = new Size(470, 90);
        txtDescription.Multiline = true;
        txtDescription.ReadOnly = true;
        txtDescription.ScrollBars = ScrollBars.Vertical;
        txtDescription.Font = new Font("Segoe UI", 10F);

        panelRight.Controls.Add(lblTitle);
        panelRight.Controls.Add(lblInfo);
        panelRight.Controls.Add(lblDesc);
        panelRight.Controls.Add(txtDescription);

        Button btnLoginBorrow = new Button();
        btnLoginBorrow.Text = "Đăng nhập để mượn";
        btnLoginBorrow.Size = new Size(210, 38);
        btnLoginBorrow.Location = new Point(215, 455);
        btnLoginBorrow.BackColor = Color.FromArgb(46, 204, 113);
        btnLoginBorrow.ForeColor = Color.White;
        btnLoginBorrow.FlatStyle = FlatStyle.Flat;
        btnLoginBorrow.FlatAppearance.BorderSize = 0;
        btnLoginBorrow.Click += BtnLoginBorrow_Click;

        Button btnClose = new Button();
        btnClose.Text = "Đóng";
        btnClose.Size = new Size(120, 38);
        btnClose.Location = new Point(440, 455);
        btnClose.BackColor = Color.FromArgb(33, 136, 198);
        btnClose.ForeColor = Color.White;
        btnClose.FlatStyle = FlatStyle.Flat;
        btnClose.FlatAppearance.BorderSize = 0;
        btnClose.Click += (s, e) => Close();

        Controls.Add(panelLeft);
        Controls.Add(panelRight);
        Controls.Add(btnLoginBorrow);
        Controls.Add(btnClose);
    }

  private
    void BookDetailForm_Load(object sender, EventArgs e) {
        string sql = @" SELECT b.Title, b.ISBN, a.AuthorName, c.CategoryName, p.PublisherName,
               b.PublishYear, b.Price, b.Location, b.TotalCopies, b.AvailableCopies, b.Description FROM Books b LEFT JOIN Authors a ON b.AuthorID = a.AuthorID LEFT JOIN Categories c ON b.CategoryID = c.CategoryID LEFT JOIN Publishers p ON b.PublisherID = p.PublisherID WHERE b.BookID = @BookID ";

                                                                                     DataTable dt = DatabaseHelper.ExecuteQuery(sql, new SqlParameter("@BookID", _bookId));
        if (dt.Rows.Count == 0)
            return;

        DataRow row = dt.Rows[0];

        string title = row["Title"].ToString();
        string isbn = row["ISBN"].ToString();
        string author = row["AuthorName"].ToString();
        string category = row["CategoryName"].ToString();
        string publisher = row["PublisherName"].ToString();
        string year = row["PublishYear"].ToString();
        string location = row["Location"].ToString();
        string desc = row["Description"].ToString();

        decimal price = 0;
        int total = 0;
        int available = 0;

        decimal.TryParse(row["Price"].ToString(), out price);
        int.TryParse(row["TotalCopies"].ToString(), out total);
        int.TryParse(row["AvailableCopies"].ToString(), out available);

        lblTitle.Text = title;
        lblInfo.Text =
            "ISBN: " + isbn + "\r\n" +
            "Tác giả: " + author + "\r\n" +
            "Thể loại: " + category + "\r\n" +
            "NXB: " + publisher + "\r\n" +
            "Năm: " + year + "\r\n" +
            "Vị trí: " + location + "\r\n" +
            "Giá trị: " + price.ToString("N0") + " đ";

        txtDescription.Text = string.IsNullOrWhiteSpace(desc) ? "Chưa có mô tả" : desc;

        if (available > 0) {
            lblStock.Text = "☑ Còn sách";
            lblStock.ForeColor = Color.Green;
        } else {
            lblStock.Text = "☒ Hết sách";
            lblStock.ForeColor = Color.Red;
        }

        lblQty.Text = "Số lượng: " + available + "/" + total + " bản";
    }

  private
    void BtnLoginBorrow_Click(object sender, EventArgs e) {
        using(LoginForm login = new LoginForm()) {
            if (login.ShowDialog() == DialogResult.OK) {
                Hide();
                using(MainForm main = new MainForm(login.FullName)) {
                    main.ShowDialog();
                }
                Show();
            }
        }
    }
}
} // namespace LibraryManagementCLean. Forms

