/*
 * Copyright 2015 - 2019 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;
import org.traccar.model.Command;

import javax.inject.Inject;

public class MeitrackProtocol extends BaseProtocol {

    @Inject
    public MeitrackProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ALARM_ARM,
                Command.TYPE_ALARM_DISARM,
                Command.TYPE_REQUEST_PHOTO,
                Command.TYPE_SEND_SMS);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new MeitrackFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new MeitrackProtocolEncoder(MeitrackProtocol.this));
                pipeline.addLast(new MeitrackProtocolDecoder(MeitrackProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new MeitrackProtocolEncoder(MeitrackProtocol.this));
                pipeline.addLast(new MeitrackProtocolDecoder(MeitrackProtocol.this));
            }
        });
    }

}
