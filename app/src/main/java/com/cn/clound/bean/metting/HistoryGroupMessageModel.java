package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 群组消息历史记录model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年6月8日 12:20:42
 */
public class HistoryGroupMessageModel extends BaseModel implements Serializable {
    private HistoryGroupMessage data;

    public HistoryGroupMessage getData() {
        return data;
    }

    public void setData(HistoryGroupMessage data) {
        this.data = data;
    }

    public class HistoryGroupMessage implements Serializable {
        private String totalCount;
        private String totalPage;
        private List<GroupMessage> result;

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public List<GroupMessage> getResult() {
            return result;
        }

        public void setResult(List<GroupMessage> result) {
            this.result = result;
        }

        public class GroupMessage implements Serializable {
            private String from;
            private String time;//发送时间
            private String timestamp;
            private String to;//群聊记录为群组id
            private String msg_id;
            private Payload payload;

            public String getMsg_id() {
                return msg_id;
            }

            public void setMsg_id(String msg_id) {
                this.msg_id = msg_id;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public Payload getPayload() {
                return payload;
            }

            public void setPayload(Payload payload) {
                this.payload = payload;
            }

            public class Payload implements Serializable {
                private Ext ext;
                private List<Body> bodies;

                public Ext getExt() {
                    return ext;
                }

                public void setExt(Ext ext) {
                    this.ext = ext;
                }

                public List<Body> getBodies() {
                    return bodies;
                }

                public void setBodies(List<Body> bodies) {
                    this.bodies = bodies;
                }

                public class Ext implements Serializable  {
                    private String extended_msg_json;

                    public String getExtended_msg_json() {
                        return extended_msg_json;
                    }

                    public void setExtended_msg_json(String extended_msg_json) {
                        this.extended_msg_json = extended_msg_json;
                    }
                }

                public class Body implements Serializable  {
                    private String addr;
                    private String filename;
                    private String lat;
                    private String length;
                    private String lng;
                    private String msg;
                    private String type;
                    private String url;

                    public String getAddr() {
                        return addr;
                    }

                    public void setAddr(String addr) {
                        this.addr = addr;
                    }

                    public String getFilename() {
                        return filename;
                    }

                    public void setFilename(String filename) {
                        this.filename = filename;
                    }

                    public String getLat() {
                        return lat;
                    }

                    public void setLat(String lat) {
                        this.lat = lat;
                    }

                    public String getLength() {
                        return length;
                    }

                    public void setLength(String length) {
                        this.length = length;
                    }

                    public String getLng() {
                        return lng;
                    }

                    public void setLng(String lng) {
                        this.lng = lng;
                    }

                    public String getMsg() {
                        return msg;
                    }

                    public void setMsg(String msg) {
                        this.msg = msg;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }
        }
    }
}
