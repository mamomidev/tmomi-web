package org.hh99.tmomi.domain.stage.controller.v1;

import java.util.List;

import org.hh99.tmomi.domain.stage.dto.rank.RankRequestDto;
import org.hh99.tmomi.domain.stage.dto.rank.RankResponseDto;
import org.hh99.tmomi.domain.stage.service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class RankController {

	private final RankService rankService;

	@GetMapping("/stage/{stageId}/seat")
	public ResponseEntity<List<RankResponseDto>> getRankListByStageId(@PathVariable Long stageId) {
		return ResponseEntity.ok(rankService.getRankListByStageId(stageId));
	}

	@PostMapping("/rank")
	public ResponseEntity<RankResponseDto> createRank(@RequestBody RankRequestDto rankRequestDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(rankService.createRank(rankRequestDto));
	}

	@PutMapping("/rank/{rankId}")
	public ResponseEntity updateRank(@PathVariable Long rankId, @RequestBody RankRequestDto rankRequestDto) {
		rankService.updateRank(rankId, rankRequestDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/rank/{rankId}")
	public ResponseEntity deleteRank(@PathVariable Long rankId) {
		rankService.deleteRank(rankId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
