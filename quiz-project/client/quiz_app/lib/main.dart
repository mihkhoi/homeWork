import 'package:flutter/material.dart';
import 'core/api.dart';
import 'auth/auth_repo.dart';
import 'subjects/subjects_repo.dart';
import 'exam/exam_repo.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Api.attachToken();
  runApp(const QuizApp());
}

class QuizApp extends StatelessWidget {
  const QuizApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Quiz',
      home: LoginPage(),
    );
  }
}

class LoginPage extends StatefulWidget { @override State<LoginPage> createState()=>_LoginPageState(); }
class _LoginPageState extends State<LoginPage> {
  final _u=TextEditingController(text:'admin'), _p=TextEditingController(text:'123456');
  final _repo=AuthRepo();
  String? _err;
  @override Widget build(BuildContext c){
    return Scaffold(
      appBar: AppBar(title: const Text('Đăng nhập')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(children: [
          TextField(controller:_u, decoration: const InputDecoration(labelText:'Username')),
          TextField(controller:_p, decoration: const InputDecoration(labelText:'Password'), obscureText:true),
          const SizedBox(height:12),
          ElevatedButton(onPressed: () async {
            final ok = await _repo.login(_u.text,_p.text);
            if(ok && mounted) Navigator.pushReplacement(c, MaterialPageRoute(builder: (_)=> const SubjectPage()));
            setState(()=>_err = ok? null : 'Sai tài khoản/mật khẩu');            
          }, child: const Text('Login')),
          if(_err!=null) Text(_err!, style: const TextStyle(color: Colors.red))
        ]),
      ),
    );
  }
}

class SubjectPage extends StatefulWidget { const SubjectPage({super.key}); @override State<SubjectPage> createState()=>_SubjectPageState(); }
class _SubjectPageState extends State<SubjectPage> {
  final repo = SubjectsRepo();
  List<Subject> data=[];
  @override void initState(){ super.initState(); repo.list().then((v){ setState(()=>data=v);});}
  @override Widget build(BuildContext c){
    return Scaffold(
      appBar: AppBar(title: const Text('Môn học')),
      body: ListView.builder(
        itemCount: data.length,
        itemBuilder: (_,i)=>ListTile(
          title: Text(data[i].name),
          subtitle: Text(data[i].code),
          onTap: ()=>Navigator.push(c, MaterialPageRoute(builder: (_)=> ExamAdminPage(subject: data[i]))),
        ),
      ),
    );
  }
}

class ExamAdminPage extends StatefulWidget {
  final Subject subject; const ExamAdminPage({super.key, required this.subject});
  @override State<ExamAdminPage> createState()=>_ExamAdminPageState();
}
class _ExamAdminPageState extends State<ExamAdminPage>{
  final examRepo=ExamRepo(); final attemptRepo=AttemptRepo();
  int? examId; Map<String,dynamic>? examData; int? attemptId; double? score;
  @override Widget build(BuildContext c){
    return Scaffold(
      appBar: AppBar(title: Text('Đề - ${widget.subject.name}')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          ElevatedButton(
            onPressed: () async {
              final id = await examRepo.createExam(
                subjectId: widget.subject.id, title: 'Đề thử', total: 1, duration: 10);
              await examRepo.generate(id);
              final d = await examRepo.getExam(id);
              setState(() { examId=id; examData=d; });
            }, child: const Text('Tạo & trộn đề 1 câu')),
          if(examData!=null) ...[
            const SizedBox(height:10),
            Text('Đề #$examId: ${(examData!['exam']['title'])}'),
            ElevatedButton(onPressed: () async {
              final id = await attemptRepo.start(examId!);
              setState(()=>attemptId=id);
            }, child: const Text('Bắt đầu làm bài')),
          ],
          if(attemptId!=null) Expanded(child: _TakeExam(examData: examData!, attemptId: attemptId!,
              onSubmit: (s){ setState(()=>score=s); })),
          if(score!=null) Text('Điểm: ${score!.toStringAsFixed(2)}')
        ]),
      ),
    );
  }
}

class _TakeExam extends StatefulWidget{
  final Map<String,dynamic> examData; final int attemptId; final Function(double) onSubmit;
  const _TakeExam({required this.examData, required this.attemptId, required this.onSubmit});
  @override State<_TakeExam> createState()=>_TakeExamState();
}
class _TakeExamState extends State<_TakeExam>{
  final attemptRepo = AttemptRepo();
  final answers = <int,int>{}; // questionId -> choiceId
  @override Widget build(BuildContext c){
    final qs = List<Map<String,dynamic>>.from(widget.examData['questions']);
    return Column(children: [
      Expanded(child: ListView.builder(
        itemCount: qs.length,
        itemBuilder: (_,i){
          final q = qs[i];
          final choices = List<Map<String,dynamic>>.from(q['choices']);
          final qid = q['id'] as int;
          return Card(child: Padding(padding: const EdgeInsets.all(12), child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Câu ${i+1}. ${q['content']}', style: const TextStyle(fontWeight: FontWeight.bold)),
              ...choices.map((c)=>RadioListTile<int>(
                title: Text(c['content']),
                value: c['id'], groupValue: answers[qid],
                onChanged: (v) async {
                  setState(()=>answers[qid]=v!);
                  await attemptRepo.answer(widget.attemptId, qid, choiceId: v);
                },
              ))
            ],
          )));
        })),
      ElevatedButton(onPressed: () async {
        final s = await attemptRepo.submit(widget.attemptId);
        widget.onSubmit(s);
      }, child: const Text('Nộp bài')),
    ]);
  }
}
