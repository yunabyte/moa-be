package com.moa.moa_server.domain.vote.dto.response.result;

import java.util.List;

public record VoteResultResponse (
        Long voteId,
        Integer userResponse,
        int totalCount,
        List<VoteOptionResult> results
) {}
