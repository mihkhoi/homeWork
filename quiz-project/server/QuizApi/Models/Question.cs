namespace QuizApi.Models;

public class Question
{
    public int Id { get; set; }
    public int SubjectId { get; set; }
    public string QType { get; set; } = "MCQ_SINGLE"; // MCQ_SINGLE/MCQ_MULTI/FILL
    public string Content { get; set; } = "";
}

public class Choice
{
    public int Id { get; set; }
    public int QuestionId { get; set; }
    public string Content { get; set; } = "";
    public bool IsCorrect { get; set; }
}
