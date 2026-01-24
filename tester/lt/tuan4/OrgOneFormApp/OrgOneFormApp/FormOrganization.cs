using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace OrgOneFormApp
{
    public partial class FormOrganization : Form
    {
        // ✅ ServerName của bạn: (LocalDB)\MSSQLLocalDB
        // Encrypt=False để tránh lỗi SSL/Encrypt trên LocalDB
        private const string ConnectionString =
            @"Server=(localdb)\MSSQLLocalDB;Database=OrgDb;Trusted_Connection=True;Encrypt=False;";

        private int? _savedOrgId = null;

        public FormOrganization()
        {
            InitializeComponent();
            btnDirector.Enabled = false; // yêu cầu: disable lúc mở form
        }

        // ===== SAVE =====
        private void btnSave_Click(object sender, EventArgs e)
        {
            errorProvider1.Clear();

            string orgName = (txtOrgName.Text ?? "").Trim();
            string address = (txtAddress.Text ?? "").Trim();
            string phone = (txtPhone.Text ?? "").Trim();
            string email = (txtEmail.Text ?? "").Trim();

            // 1) Validate theo đề
            Dictionary<string, string> errors = ValidateInput(orgName, phone, email);
            if (errors.Count > 0)
            {
                ShowValidationErrors(errors);
                MessageBox.Show("Please fix validation errors.", "Validation",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            try
            {
                // 2) Check duplicate OrgName
                if (OrgNameExists(orgName))
                {
                    MessageBox.Show("Organization Name already exists", "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                // 3) Insert DB
                int newId = InsertOrganization(orgName, address, phone, email);
                _savedOrgId = newId;

                btnDirector.Enabled = true; // Save OK -> enable Director
                MessageBox.Show("Save successfully", "Success",
                    MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (SqlException ex)
            {
                MessageBox.Show("Database error: " + ex.Message, "DB Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Unexpected error: " + ex.Message, "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // ===== DIRECTOR =====
        private void btnDirector_Click(object sender, EventArgs e)
        {
            if (_savedOrgId == null)
            {
                MessageBox.Show("Please save Organization first.", "Info",
                    MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            // Bài yêu cầu mở màn hình Director, bạn chưa làm thì demo message:
            MessageBox.Show("Open Director Management\nOrgID = " + _savedOrgId +
                            "\nOrgName = " + txtOrgName.Text.Trim(),
                "Director", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        // ===== BACK =====
        private void btnBack_Click(object sender, EventArgs e)
        {
            Close();
        }

        // ======================
        // BUSINESS: VALIDATION
        // ======================
        private static Dictionary<string, string> ValidateInput(string orgName, string phone, string email)
        {
            var errors = new Dictionary<string, string>();

            // OrgName: required, 3-255
            if (string.IsNullOrWhiteSpace(orgName))
                errors["OrgName"] = "Organization Name is required.";
            else if (orgName.Length < 3 || orgName.Length > 255)
                errors["OrgName"] = "Organization Name must be 3–255 characters.";

            // Phone: optional, 9-12 digits, digits only
            if (!string.IsNullOrWhiteSpace(phone))
            {
                if (phone.Length < 9 || phone.Length > 12)
                {
                    errors["Phone"] = "Phone must be 9–12 digits.";
                }
                else
                {
                    for (int i = 0; i < phone.Length; i++)
                    {
                        if (!char.IsDigit(phone[i]))
                        {
                            errors["Phone"] = "Phone must contain digits only.";
                            break;
                        }
                    }
                }
            }

            // Email: optional, đúng format
            if (!string.IsNullOrWhiteSpace(email))
            {
                var emailRegex = new Regex(@"^[^@\s]+@[^@\s]+\.[^@\s]+$");
                if (!emailRegex.IsMatch(email))
                    errors["Email"] = "Email format is invalid.";
            }

            return errors;
        }

        private void ShowValidationErrors(Dictionary<string, string> errors)
        {
            string msg;

            if (errors.TryGetValue("OrgName", out msg))
                errorProvider1.SetError(txtOrgName, msg);

            if (errors.TryGetValue("Phone", out msg))
                errorProvider1.SetError(txtPhone, msg);

            if (errors.TryGetValue("Email", out msg))
                errorProvider1.SetError(txtEmail, msg);
        }

        // ======================
        // DB: SQL SERVER LocalDB
        // ======================
        private static bool OrgNameExists(string orgName)
        {
            using (var con = new SqlConnection(ConnectionString))
            {
                con.Open();
                using (var cmd = con.CreateCommand())
                {
                    cmd.CommandText = "SELECT 1 FROM dbo.ORGANIZATION WHERE OrgName = @name";
                    cmd.Parameters.AddWithValue("@name", orgName);
                    return cmd.ExecuteScalar() != null;
                }
            }
        }

        private static int InsertOrganization(string orgName, string address, string phone, string email)
        {
            using (var con = new SqlConnection(ConnectionString))
            {
                con.Open();
                using (var cmd = con.CreateCommand())
                {
                    cmd.CommandText = @"
INSERT INTO dbo.ORGANIZATION(OrgName, Address, Phone, Email, CreatedDate)
OUTPUT INSERTED.OrgID
VALUES(@name, @addr, @phone, @email, @created);
";
                    cmd.Parameters.AddWithValue("@name", orgName);
                    cmd.Parameters.AddWithValue("@addr", string.IsNullOrWhiteSpace(address) ? (object)DBNull.Value : address);
                    cmd.Parameters.AddWithValue("@phone", string.IsNullOrWhiteSpace(phone) ? (object)DBNull.Value : phone);
                    cmd.Parameters.AddWithValue("@email", string.IsNullOrWhiteSpace(email) ? (object)DBNull.Value : email);
                    cmd.Parameters.AddWithValue("@created", DateTime.Now);

                    return (int)cmd.ExecuteScalar();
                }
            }
        }
    }
}
