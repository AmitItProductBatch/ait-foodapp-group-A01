package com.ait.app.service;

import com.ait.app.dto.FeedbackDto;
import com.ait.app.model.Feedback;

public interface FeedbackService {

	Feedback createFeedBack(FeedbackDto dto);
}
