namespace QuizApi.Models;

public class Exam
{
    public int Id { get; set; }
    public int SubjectId { get; set; }
    public string Title { get; set; } = "";
    public int DurationMinutes { get; set; }
    public int TotalQuestions { get; set; }
    public bool ShuffleQuestions { get; set; } = true;
    public bool ShuffleChoices  { get; set; } = true;
    public int CreatedBy { get; set; }
    public DateTime CreatedAt { get; set; }
}

public class ExamQuestion
{
    public int Id { get; set; }
    public int ExamId { get; set; }
    public int QuestionId { get; set; }
    public int Ord { get; set; }
}
