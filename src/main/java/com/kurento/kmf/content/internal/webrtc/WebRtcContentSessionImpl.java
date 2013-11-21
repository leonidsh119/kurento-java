/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.kurento.kmf.content.internal.webrtc;

import javax.servlet.AsyncContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kurento.kmf.common.exception.Assert;
import com.kurento.kmf.content.ContentCommand;
import com.kurento.kmf.content.ContentCommandResult;
import com.kurento.kmf.content.WebRtcContentHandler;
import com.kurento.kmf.content.WebRtcContentSession;
import com.kurento.kmf.content.internal.ContentSessionManager;
import com.kurento.kmf.content.internal.base.AbstractSdpBasedMediaRequest;
import com.kurento.kmf.content.jsonrpc.JsonRpcRequest;
import com.kurento.kmf.media.MediaPipeline;
import com.kurento.kmf.media.SdpEndPoint;
import com.kurento.kmf.media.WebRtcEndPoint;

/**
 * 
 * Request implementation for WebRTC.
 * 
 * @author Luis López (llopez@gsyc.es)
 * @version 1.0.0
 */
public class WebRtcContentSessionImpl extends AbstractSdpBasedMediaRequest
		implements WebRtcContentSession {

	private static final Logger log = LoggerFactory
			.getLogger(WebRtcContentSessionImpl.class);

	public WebRtcContentSessionImpl(WebRtcContentHandler handler,
			ContentSessionManager manager, AsyncContext asyncContext,
			String contentId) {
		super(handler, manager, asyncContext, contentId);
	}

	@Override
	protected SdpEndPoint buildSdpEndPoint(MediaPipeline mediaPipeline) {
		return mediaPipeline.newWebRtcEndPoint().build();
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

	@Override
	protected void processStartJsonRpcRequest(AsyncContext asyncCtx,
			JsonRpcRequest message) {
		Assert.notNull(
				initialJsonRequest.getParams().getSdp(),
				"SDP cannot be null on message with method "
						+ message.getMethod(), 10005);
		super.processStartJsonRpcRequest(asyncCtx, message);
	}

	@Override
	public WebRtcContentHandler getHandler() {
		return (WebRtcContentHandler) super.getHandler();
	}

	@Override
	protected void interalRawCallToOnSessionTerminated(int code,
			String description) throws Exception {
		getHandler().onSessionTerminated(this, code, description);
	}

	@Override
	protected void interalRawCallToOnContentStarted() throws Exception {
		getHandler().onContentStarted(this);
	}

	@Override
	protected void interalRawCallToOnContentError(int code, String description)
			throws Exception {
		getHandler().onSessionError(this, code, description);
	}

	@Override
	protected void internalRawCallToOnContentRequest() throws Exception {
		getHandler().onContentRequest(this);
	}

	@Override
	protected void internalRawCallToOnUncaughtExceptionThrown(Throwable t)
			throws Exception {
		getHandler().onUncaughtException(this, t);

	}

	@Override
	protected ContentCommandResult interalRawCallToOnContentCommand(
			ContentCommand command) throws Exception {
		return getHandler().onContentCommand(this, command);
	}

	@Override
	public WebRtcEndPoint getSessionEndPoint() {
		return (WebRtcEndPoint) super.getSessionEndPoint();
	}
}
