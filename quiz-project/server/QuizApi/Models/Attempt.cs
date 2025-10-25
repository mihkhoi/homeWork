namespace QuizApi.Models;

public class Attempt
{
    public long Id { get; set; }
    public int ExamId { get; set; }
    public int UserId { get; set; }
    public DateTime StartedAt { get; set; }
    public DateTime? SubmittedAt { get; set; }
    public decimal? Score { get; set; }
}

public class AttemptAnswer
{
    public long Id { get; set; }
    public long AttemptId { get; set; }
    public int QuestionId { get; set; }
    public int? ChoiceId { get; set; }
    public string? FillText { get; set; }
}
