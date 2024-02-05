package org.hh99.tmomi.global.exception.message;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	//TODO 유저,티켓,행사,공연장,좌석 에러 번호 설정?
	// 티켓
	NOT_EXIST_TICKET("티켓 정보가 존재하지 않습니다."),

	// 공연장
	NOT_EXIST_STAGE("공연장 정보가 존재하지 않습니다."),

	// 행사
	NOT_EXIST_EVENT("공연 정보가 존재하지 않습니다."),
	NOT_EXIST_EVENT_TIME("공연 시간이 존재하지 않습니다."),

	// 유저
	NOT_EXIST_USER("유저 정보가 존재하지 않습니다."),

	// 좌석
	NOT_EXIST_SEAT("좌석 정보가 존재하지 않습니다."),

	// 등급
	NOT_EXIST_RANK("등급 정보가 존재하지 않습니다.");

	private final String message;

	ExceptionCode(String message) {
		this.message = message;
	}

}