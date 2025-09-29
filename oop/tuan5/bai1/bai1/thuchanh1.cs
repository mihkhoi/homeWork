using System;
using System.Collections.Generic;
using System.Diagnostics.Eventing.Reader;
using System.Linq;
using System.IO;
using System.Text;
using System.Threading.Tasks;

namespace bai1
{
    internal class thuchanh1
    {
        static void Main(string[] args)
        {
            //Bai1
            //Console.Write("Nhap vao chieu dai: ");
            //double a = Convert.ToDouble(Console.ReadLine());
            //Console.Write("Nhap vao chieu rong: ");
            //double b = Convert.ToDouble(Console.ReadLine());

            //double s = a * b;
            //double p = (a + b) * 2;

            //Console.WriteLine("Chu vi: " + p);
            //Console.WriteLine("Dien tich: " + s);
            //Console.Write("Nhap so nguyen a: ");
            //int a = Convert.ToInt32(Console.ReadLine());
            //Console.Write("Nhap so nguyen b: ");
            //int b = Convert.ToInt32(Console.ReadLine());
            //int max = 0;
            //if (a > b)
            //{
            //    max = a;
            //}
            //else
            //{
            //    max = b;
            //}
            //Console.Write(max);
            //Console.Write("Nhap so nguyen a: ");
            //int a = Convert.ToInt32(Console.ReadLine());
            //Console.Write("Nhap so nguyen b: ");
            //int b = Convert.ToInt32(Console.ReadLine());
            //Console.Write("Nhap so nguyen c: ");
            //int c = Convert.ToInt32(Console.ReadLine());
            //if ((a > b) && (a > c))
            //{
            //    Console.Write(a);
            //}
            //else if ((b > a) && (b > c))
            //{
            //    Console.Write(b);
            //}
            //else
            //{
            //    Console.Write(c);
            //}
            //Console.Write(" nhap vao nam: ");
            //int nam = Convert.ToInt32(Console.ReadLine());
            //Console.Write(" nhap vao thang: ");
            //int thang = Convert.ToInt32(Console.ReadLine());
            //// Tìm số ngày trong tháng
            //switch (thang)
            //{
            //    case 1:
            //    case 3:
            //    case 5:
            //    case 7:
            //    case 8:
            //    case 10:
            //    case 12:
            //        Console.WriteLine("Thang co 31 ngay!");
            //        break;
            //    case 4:
            //    case 6:
            //    case 9:
            //    case 11:
            //        Console.WriteLine("Thang co 30 ngay!");
            //        break;
            //    case 2:
            //        if ((nam % 400 == 0) || ((nam % 4 == 0) && (nam % 100 != 0))) {
            //        }else
            //        {
            //        }
            //        Console.WriteLine("Thang co 29 ngay!");

            //        Console.WriteLine("Thang co 28 ngay!");
            //        break;
            //}
            //Console.Write("Nhap so nguyen n: ");
            //int n = Convert.ToInt32(Console.ReadLine());
            //if(n%2 == 0)
            //{
            //    Console.Write("n la so chan");
            //}
            //else
            //{
            //    Console.Write("n la so le");
            //}
            //if(n < 0)
            //{
            //    Console.Write("n la so am");
            //}
            //else
            //{
            //    Console.Write("n la so duong");
            //}
            //double a;
            //do
            //{
            //    Console.Write("Nhap chieu dai a: ");
            //    a = Convert.ToDouble(Console.ReadLine());
            //} while (a < 0);
            //double b;
            //do
            //{
            //    Console.Write("Nhap chieu rong b: ");
            //    b = Convert.ToDouble(Console.ReadLine());
            //} while (b < 0);
            //double s = a * b;
            //double p = (a + b) * 2;
            //Console.Write("Chu vi: " + p);
            //Console.Write("Dien tich: " + s);
            //Console.Write("Nhap chieu dai a: ");
            //double a = Convert.ToDouble(Console.ReadLine());

            //Console.Write("Nhap chieu dai b: ");
            //double b = Convert.ToDouble(Console.ReadLine());

            //Console.Write("Nhap chieu dai c: ");
            //double c = Convert.ToDouble(Console.ReadLine());

            //if ((a + b > c) && (a + c > b) && (b + c > a))
            //{
            //    double p = (a + b + c) / 2; // nửa chu vi
            //    double s = Math.Sqrt(p * (p - a) * (p - b) * (p - c));

            //    Console.WriteLine("Chu vi: " + (a + b + c));
            //    Console.WriteLine("Dien tich: " + s);
            //}
            //else
            //{
            //    Console.WriteLine("Ba cạnh nhập vào không tạo thành tam giác!");
            //}
            //Console.Write("Nhap a: ");
            //double a = Convert.ToDouble(Console.ReadLine());

            //Console.Write("Nhap b: ");
            //double b = Convert.ToDouble(Console.ReadLine());

            //Console.Write("Nhap c: ");
            //double c = Convert.ToDouble(Console.ReadLine());

            //if (a == 0)
            //{
            //    // Trường hợp phương trình trở thành bx + c = 0
            //    if (b == 0)
            //    {
            //        if (c == 0)
            //            Console.WriteLine("Phuong trinh co vo so nghiem.");
            //        else
            //            Console.WriteLine("Phuong trinh vo nghiem.");
            //    }
            //    else
            //    {
            //        double x = -c / b;
            //        Console.WriteLine("Phuong trinh bac nhat, nghiem: x = " + x);
            //    }
            //}
            //else
            //{
            //    // Phương trình bậc 2
            //    double delta = b * b - 4 * a * c;

            //    if (delta < 0)
            //    {
            //        Console.WriteLine("Phuong trinh vo nghiem thuc.");
            //    }
            //    else if (delta == 0)
            //    {
            //        double x = -b / (2 * a);
            //        Console.WriteLine("Phuong trinh co nghiem kep: x = " + x);
            //    }
            //    else
            //    {
            //        double x1 = (-b + Math.Sqrt(delta)) / (2 * a);
            //        double x2 = (-b - Math.Sqrt(delta)) / (2 * a);
            //        Console.WriteLine("Phuong trinh co 2 nghiem phan biet:");
            //        Console.WriteLine("x1 = " + x1);
            //        Console.WriteLine("x2 = " + x2);
            //    }
            //}
            //Console.Write("Nhap so phan tu cua mang: ");
            //int n = Convert.ToInt32(Console.ReadLine());

            //int[] arr = new int[n];

            //// Nhap cac phan tu
            //for (int i = 0; i < n; i++)
            //{
            //    Console.Write($"Nhap phan tu arr[{i}]: ");
            //    arr[i] = Convert.ToInt32(Console.ReadLine());
            //}

            //// Tinh tong
            //int tong = 0;
            //for (int i = 0; i < n; i++)
            //{
            //    tong += arr[i];
            //}

            //Console.WriteLine("Tong cac phan tu trong mang la: " + tong);
            // Đọc dữ liệu từ file
            string path = @"D:\Code\oop\tuan5\bai1\bai1\input_array.txt";
            if (!File.Exists(path))
            {
                Console.WriteLine("Khong tim thay file " + path);
                return;
            }

            // Đọc tất cả số nguyên từ file (cách nhau bởi khoảng trắng hoặc xuống dòng)
            int[] arr = File.ReadAllText(path)
                            .Split(new char[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries)
                            .Select(int.Parse)
                            .ToArray();

            Console.WriteLine("Mang ban dau: " + string.Join(" ", arr));

            // Selection Sort
            for (int i = 0; i < arr.Length - 1; i++)
            {
                int minIndex = i;
                for (int j = i + 1; j < arr.Length; j++)
                {
                    if (arr[j] < arr[minIndex])
                    {
                        minIndex = j;
                    }
                }
                // Hoán đổi phần tử nhỏ nhất với arr[i]
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }

            Console.WriteLine("Mang sau khi sap xep: " + string.Join(" ", arr));

            // Ghi kết quả ra file mới
            File.WriteAllText("output_array.txt", string.Join(" ", arr));
            Console.WriteLine("Da ghi ket qua ra file output_array.txt");
            //Nhập mảng đã sắp xếp sẵn
            //Console.Write("Nhap so phan tu cua mang: ");
            //int n = Convert.ToInt32(Console.ReadLine());

            //int[] arr = new int[n];
            //Console.WriteLine("Nhap cac phan tu (theo thu tu tang dan):");
            //for (int i = 0; i < n; i++)
            //{
            //    arr[i] = Convert.ToInt32(Console.ReadLine());
            //}

            //Console.Write("Nhap so can chen: ");
            //int x = Convert.ToInt32(Console.ReadLine());

            //// Mảng mới có thêm 1 phần tử
            //int[] newArr = new int[n + 1];
            //bool inserted = false;
            //int k = 0; // chỉ số cho mảng mới

            //for (int i = 0; i < n; i++)
            //{
            //    if (!inserted && x <= arr[i])
            //    {
            //        newArr[k++] = x;   // chèn x vào đây
            //        inserted = true;
            //    }
            //    newArr[k++] = arr[i];
            //}

            //// Nếu x lớn hơn tất cả phần tử
            //if (!inserted)
            //{
            //    newArr[k] = x;
            //}

            //Console.WriteLine("Mang sau khi chen:");
            //Console.WriteLine(string.Join(" ", newArr));
        }
    }
}
