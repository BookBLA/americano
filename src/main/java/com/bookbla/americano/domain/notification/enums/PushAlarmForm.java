package com.bookbla.americano.domain.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushAlarmForm {

    POSTCARD_SEND("띵동~\uD83D\uDC8C 엽서가 도착했어요!", "%s님이 엽서를 보냈어요! 지금 확인해 보세요~\uD83E\uDD70"),
    POSTCARD_ACCEPT("축하합니다\uD83E\uDD73\uD83E\uDD73 매칭에 성공하셨습니다~!!\uD83D\uDC95", "%s님이 엽서를 수락했어요! 지금 바로 채팅해보세요~\uD83E\uDD70"),

    ADMIN_VERIFICATION_ACCEPT("💌가입 승인이 완료되었습니다!", "스타일을 입력하고 매칭을 시작해보세요"),
    ADMIN_PROFILE_IMAGE_REJECT("\uD83D\uDE22프로필을 다시 등록해주세요!", "프로필 사진을 다시 등록 후 매칭을 시작해보세요!"),
    ADMIN_STUDENT_ID_IMAGE_ACCEPT("학생증  인증에 성공했어요!", "무료 책갈피 35개 받고 매칭을 시작해 보세요!"),
    ADMIN_STUDENT_ID_IMAGE_REJECT("\uD83D\uDE22학생증을 다시 등록해주세요!", "학생증의 모든 정보(이름,학번,학과,얼굴)가 선명하게 보이도록 해주세요! "),
    ADMIN_OPEN_KAKAO_ROOM_REJECT("\uD83D\uDE22오픈채팅방을 다시 등록해주세요!", "링크를 다시 등록 후 매칭을 시작해보세요!"),

    INVITATION_SUCCESS("친구가 가입에 성공했어요!", "지금 접속해서 무료 책갈피를 받아가세요!"),
    ;


    private final String title;
    private final String body;
}
