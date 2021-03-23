package com.ml.model.response;

import com.ml.model.MutantCheckupStat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetStatsResponse {

	private MutantCheckupStat data;

}
