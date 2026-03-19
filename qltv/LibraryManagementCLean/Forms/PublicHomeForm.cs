using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms {
public
class PublicHomeForm : Form {
  private
    TextBox txtSearch;
  private
    ComboBox cboCategory;
  private
    Label lblTotal;
  private
    FlowLayoutPanel flowTopBooks;
  private
    FlowLayoutPanel flowCategories;
  private
    FlowLayoutPanel flowBooks;

  public
    PublicHomeForm() {
        BuildUi();
        Load += PublicHomeForm_Load;
    }

  private
    void BuildUi() {
        Text = "Thư Viện Sách - Tra cứu công khai";
        WindowState = FormWindowState.Maximized;
        BackColor = Color.FromArgb(236, 239, 241);

        Panel panelHeader = new Panel();
        panelHeader.Dock = DockStyle.Top;
        panelHeader.Height = 72;
        panelHeader.BackColor = Color.FromArgb(33, 136, 198);

        Label lblTitle = new Label();
        lblTitle.Text = "📚 THƯ VIỆN SÁCH - Tra cứu  Mượn sách online";
        lblTitle.ForeColor = Color.White;
        lblTitle.Font = new Font("Segoe UI", 20F, FontStyle.Bold);
        lblTitle.AutoSize = true;
        lblTitle.Location = new Point(18, 16);

        Button btnRegister = new Button();
        btnRegister.Text = "Đăng ký";
        btnRegister.Size = new Size(100, 34);
        btnRegister.Location = new Point(1260, 18);
        btnRegister.BackColor = Color.MediumOrchid;
        btnRegister.ForeColor = Color.White;
        btnRegister.FlatStyle = FlatStyle.Flat;
        btnRegister.FlatAppearance.BorderSize = 0;
        btnRegister.Anchor = AnchorStyles.Top | AnchorStyles.Right;
        btnRegister.Click += (s, e) =>{
            using(RegisterGuideForm f = new RegisterGuideForm()){
                f.ShowDialog();
    }
};

Button btnLogin = new Button();
btnLogin.Text = "Đăng nhập";
btnLogin.Size = new Size(110, 34);
btnLogin.Location = new Point(1370, 18);
btnLogin.BackColor = Color.FromArgb(46, 204, 113);
btnLogin.ForeColor = Color.White;
btnLogin.FlatStyle = FlatStyle.Flat;
btnLogin.FlatAppearance.BorderSize = 0;
btnLogin.Anchor = AnchorStyles.Top | AnchorStyles.Right;
btnLogin.Click += BtnLogin_Click;

panelHeader.Controls.Add(lblTitle);
panelHeader.Controls.Add(btnRegister);
panelHeader.Controls.Add(btnLogin);

Panel panelSearch = new Panel();
panelSearch.Dock = DockStyle.Top;
panelSearch.Height = 56;
panelSearch.BackColor = Color.WhiteSmoke;

Label lblSearch = new Label();
lblSearch.Text = "Tìm kiếm:";
lblSearch.AutoSize = true;
lblSearch.Font = new Font("Segoe UI", 11F);
lblSearch.Location = new Point(18, 18);

txtSearch = new TextBox();
txtSearch.PlaceholderText = "Nhập tên sách, tác giả, ISBN...";
txtSearch.Location = new Point(92, 14);
txtSearch.Width = 250;
txtSearch.Font = new Font("Segoe UI", 10F);
txtSearch.TextChanged += (s, e) => LoadBooks();

Label lblCat = new Label();
lblCat.Text = "Thể loại:";
lblCat.AutoSize = true;
lblCat.Font = new Font("Segoe UI", 11F);
lblCat.Location = new Point(365, 18);

cboCategory = new ComboBox();
cboCategory.Location = new Point(435, 14);
cboCategory.Width = 180;
cboCategory.DropDownStyle = ComboBoxStyle.DropDownList;
cboCategory.Font = new Font("Segoe UI", 10F);
cboCategory.SelectedIndexChanged += (s, e) => LoadBooks();

lblTotal = new Label();
lblTotal.Text = "Tổng: 0 sách";
lblTotal.AutoSize = true;
lblTotal.Font = new Font("Segoe UI", 11F, FontStyle.Bold);
lblTotal.ForeColor = Color.FromArgb(33, 136, 198);
lblTotal.Location = new Point(645, 18);

panelSearch.Controls.Add(lblSearch);
panelSearch.Controls.Add(txtSearch);
panelSearch.Controls.Add(lblCat);
panelSearch.Controls.Add(cboCategory);
panelSearch.Controls.Add(lblTotal);

Panel panelBody = new Panel();
panelBody.Dock = DockStyle.Fill;
panelBody.AutoScroll = true;
panelBody.BackColor = Color.FromArgb(236, 239, 241);

Label lblTopHeader = new Label();
lblTopHeader.Text = "TOP SÁCH MỚI";
lblTopHeader.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
lblTopHeader.AutoSize = true;
lblTopHeader.Location = new Point(18, 20);

flowTopBooks = new FlowLayoutPanel();
flowTopBooks.Location = new Point(18, 60);
flowTopBooks.Size = new Size(1450, 165);
flowTopBooks.AutoScroll = true;

Label lblCategoriesHeader = new Label();
lblCategoriesHeader.Text = "THỂ LOẠI SÁCH";
lblCategoriesHeader.Font = new Font("Segoe UI", 18F, FontStyle.Bold);
lblCategoriesHeader.AutoSize = true;
lblCategoriesHeader.Location = new Point(18, 240);

flowCategories = new FlowLayoutPanel();
flowCategories.Location = new Point(18, 280);
flowCategories.Size = new Size(1450, 70);
flowCategories.AutoScroll = true;

flowBooks = new FlowLayoutPanel();
flowBooks.Location = new Point(18, 370);
flowBooks.Size = new Size(1450, 520);
flowBooks.AutoScroll = true;

panelBody.Controls.Add(lblTopHeader);
panelBody.Controls.Add(flowTopBooks);
panelBody.Controls.Add(lblCategoriesHeader);
panelBody.Controls.Add(flowCategories);
panelBody.Controls.Add(flowBooks);

Controls.Add(panelBody);
Controls.Add(panelSearch);
Controls.Add(panelHeader);
} // namespace LibraryManagementCLean. Forms

private
void PublicHomeForm_Load(object sender, EventArgs e) {
    LoadCategoryCombo();
    LoadCategoryButtons();
    LoadBooks();
}

private
void LoadCategoryCombo() {
    DataTable dt = DatabaseHelper.ExecuteQuery("SELECT CategoryID, CategoryName FROM Categories WHERE IsActive = 1 ORDER BY CategoryName");
    DataTable table = dt.Clone();
    table.Rows.Add(0, "-- Tất cả thể loại --");

    foreach (DataRow row in dt.Rows)
        table.ImportRow(row);

    cboCategory.DataSource = table;
    cboCategory.DisplayMember = "CategoryName";
    cboCategory.ValueMember = "CategoryID";
}

private
void LoadCategoryButtons() {
    flowCategories.Controls.Clear();

    Button btnAll = CreateCategoryButton("Tất cả", 0);
    flowCategories.Controls.Add(btnAll);

    DataTable dt = DatabaseHelper.ExecuteQuery("SELECT CategoryID, CategoryName FROM Categories WHERE IsActive = 1 ORDER BY CategoryName");
    foreach (DataRow row in dt.Rows) {
        int id = Convert.ToInt32(row["CategoryID"]);
        string name = row["CategoryName"].ToString();
        flowCategories.Controls.Add(CreateCategoryButton(name, id));
    }
}

private
Button CreateCategoryButton(string text, int categoryId) {
    Button btn = new Button();
    btn.Text = text;
    btn.Tag = categoryId;
    btn.Width = 130;
    btn.Height = 36;
    btn.Margin = new Padding(6);
    btn.BackColor = Color.FromArgb(33, 136, 198);
    btn.ForeColor = Color.White;
    btn.FlatStyle = FlatStyle.Flat;
    btn.FlatAppearance.BorderSize = 0;

    btn.Click += (s, e) => {
        cboCategory.SelectedValue = categoryId;
        LoadBooks();
    };

    return btn;
}

private
void LoadBooks() {
    int categoryId = 0;
    if (cboCategory.SelectedValue != null)
        int.TryParse(cboCategory.SelectedValue.ToString(), out categoryId);

    string sql = @" SELECT b.BookID, b.ISBN, b.Title, a.AuthorName, c.CategoryName,
           p.PublisherName, b.PublishYear, b.AvailableCopies, b.TotalCopies, b.Location FROM Books b LEFT JOIN Authors a ON b.AuthorID = a.AuthorID LEFT JOIN Categories c ON b.CategoryID = c.CategoryID LEFT JOIN Publishers p ON b.PublisherID = p.PublisherID WHERE b.IsActive = 1 AND(@Keyword = N'' OR b.Title LIKE N '%' + @Keyword + N '%' OR a.AuthorName LIKE N '%' + @Keyword + N '%' OR b.ISBN LIKE N '%' + @Keyword + N '%') AND(@CategoryID = 0 OR b.CategoryID = @CategoryID) ORDER BY b.BookID DESC ";

                                                                                                                                                                                                                                                                                     DataTable dt = DatabaseHelper.ExecuteQuery(sql, new SqlParameter("@Keyword", txtSearch.Text.Trim()), new SqlParameter("@CategoryID", categoryId));

    lblTotal.Text = "Tổng: " + dt.Rows.Count + " sách";

    flowBooks.Controls.Clear();
    flowTopBooks.Controls.Clear();

    int topCount = 0;

    foreach (DataRow row in dt.Rows) {
        int bookId = Convert.ToInt32(row["BookID"]);
        string title = row["Title"].ToString();
        string author = row["AuthorName"].ToString();
        string category = row["CategoryName"].ToString();
        string isbn = row["ISBN"].ToString();
        int available = Convert.ToInt32(row["AvailableCopies"]);
        int total = Convert.ToInt32(row["TotalCopies"]);

        Panel card = CreateBookCard(bookId, title, author, category, isbn, available, total, false);
        flowBooks.Controls.Add(card);

        if (topCount < 3) {
            Panel topCard = CreateBookCard(bookId, title, author, category, isbn, available, total, true);
            flowTopBooks.Controls.Add(topCard);
            topCount++;
        }
    }
}

private
Panel CreateBookCard(int bookId, string title, string author, string category, string isbn, int available, int total, bool compact) {
    Panel card = new Panel();
    card.BackColor = Color.White;
    card.Cursor = Cursors.Hand;
    card.Margin = new Padding(12);

    if (compact)
        card.Size = new Size(220, 135);
    else
        card.Size = new Size(190, 270);

    Panel cover = new Panel();
    cover.BackColor = Color.FromArgb(245, 245, 245);
    cover.BorderStyle = BorderStyle.FixedSingle;

    if (compact) {
        cover.Location = new Point(12, 12);
        cover.Size = new Size(80, 110);
    } else {
        cover.Location = new Point(12, 12);
        cover.Size = new Size(166, 150);
    }

    Label lblIcon = new Label();
    lblIcon.Text = "📖";
    lblIcon.Font = new Font("Segoe UI Emoji", compact ? 24F : 34F);
    lblIcon.AutoSize = true;
    lblIcon.Location = compact ? new Point(18, 35) : new Point(50, 48);
    cover.Controls.Add(lblIcon);

    card.Controls.Add(cover);

    if (compact) {
        Label lblTitle = new Label();
        lblTitle.Text = title;
        lblTitle.Font = new Font("Segoe UI", 11F, FontStyle.Bold);
        lblTitle.AutoSize = false;
        lblTitle.Size = new Size(110, 42);
        lblTitle.Location = new Point(102, 12);

        Label lblAuthor = new Label();
        lblAuthor.Text = author;
        lblAuthor.Font = new Font("Segoe UI", 10F);
        lblAuthor.ForeColor = Color.Gray;
        lblAuthor.AutoSize = false;
        lblAuthor.Size = new Size(105, 22);
        lblAuthor.Location = new Point(102, 65);

        Label lblStock = new Label();
        lblStock.Text = "Còn " + available + " cuốn";
        lblStock.Font = new Font("Segoe UI", 10F, FontStyle.Bold);
        lblStock.ForeColor = Color.Green;
        lblStock.AutoSize = true;
        lblStock.Location = new Point(102, 92);

        card.Controls.Add(lblTitle);
        card.Controls.Add(lblAuthor);
        card.Controls.Add(lblStock);
    } else {
        Label lblTitle = new Label();
        lblTitle.Text = title;
        lblTitle.Font = new Font("Segoe UI", 11F, FontStyle.Bold);
        lblTitle.AutoSize = false;
        lblTitle.Size = new Size(166, 36);
        lblTitle.Location = new Point(12, 170);

        Label lblAuthor = new Label();
        lblAuthor.Text = "✍ " + author;
        lblAuthor.Font = new Font("Segoe UI", 10F);
        lblAuthor.ForeColor = Color.Gray;
        lblAuthor.AutoSize = true;
        lblAuthor.Location = new Point(12, 210);

        Label lblCategory = new Label();
        lblCategory.Text = "🗂 " + category;
        lblCategory.Font = new Font("Segoe UI", 10F);
        lblCategory.ForeColor = Color.Gray;
        lblCategory.AutoSize = true;
        lblCategory.Location = new Point(12, 232);

        Label lblStock = new Label();
        lblStock.Text = "☑ Còn " + available + " cuốn";
        lblStock.Font = new Font("Segoe UI", 10F, FontStyle.Bold);
        lblStock.ForeColor = Color.Green;
        lblStock.AutoSize = true;
        lblStock.Location = new Point(12, 252);

        card.Controls.Add(lblTitle);
        card.Controls.Add(lblAuthor);
        card.Controls.Add(lblCategory);
        card.Controls.Add(lblStock);
    }

    card.Click += (s, e) => OpenBookDetail(bookId);
    foreach (Control ctl in card.Controls)
        ctl.Click += (s, e) => OpenBookDetail(bookId);

    return card;
}

private
void OpenBookDetail(int bookId) {
    using(BookDetailForm f = new BookDetailForm(bookId)) {
        f.ShowDialog();
    }
}

private
void BtnLogin_Click(object sender, EventArgs e) {
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
}

