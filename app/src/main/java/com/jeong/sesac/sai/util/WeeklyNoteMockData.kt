package com.jeong.sesac.sai.util

import com.jeong.sesac.sai.R

class WeeklyNoteMockData {
    companion object {
        val notesList = mutableListOf(
            WeeklyNotesInfo(
                hint_img = R.drawable.note01,
                date = "2024-12-10",
                note_content = "제가 읽고 너무 감명깊어서 눈물이 줄줄 손이 벌벌",
                library = "중앙도서관",
                hints = listOf("교양도서임", "안 읽으면 후회", "최근 작가가 노벨상 받음"),
                likes = 2
            ),
            WeeklyNotesInfo(
                hint_img = R.drawable.note02,
                date = "2024-12-11",
                library = "청량리도서관",
                note_content = "파랑새가 나오는 책인데요 어쩌구 저ㄷ쩌구우남어나ㅣㅓ아니머아니머인ㅁ",
                hints = listOf("베셀에 올랐음", "인문학 강의에 꼭 나오는 내용임", "책에 파랑새가 나옴"),
                likes = 7
            ),
            WeeklyNotesInfo(
                hint_img = R.drawable.note03,
                date = "2024-12-11",
                library = "행복도서관",
                note_content = "ㅋㅋㅋㅋ아니 이걸 아직도 안 읽음??",
                hints = listOf("작가는 한강", "근데 사람들이 잘 안 읽음", "마이너한 책"),
                likes = 5

            ),
            WeeklyNotesInfo(
                hint_img = R.drawable.note04,
                date = "2024-12-11",
                library = "새싹리도서관",
                note_content = "sdㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ",
                hints = listOf("코틀린 읽는 사람이면 읽어야함", "제일 기초적인 책", "기술도서쪽에 있음"),
                likes = 56
            ),
            WeeklyNotesInfo(
                hint_img = R.drawable.note05,
                date = "2024-12-11",
                library = "아아도서관",
                note_content = "zdzddddddzdsdjsadhsahfajksfgsagfsagfasglk",
                hints = listOf("제목에 치즈가 들어감", "30주 연속 베스트셀러에 오름", "누가 훔쳐감"),
                likes = 35
            )
        )
    }
}