package com.ren.factoring.flow.models.enmu;

public enum ResultCode implements IReturnCode {
    /**
     * 失败状态码
     */
    FAILURE(-1, "失败"),
    /**
     * 成功状态码
     */
    SUCCESS(1, "成功"),
    /**
     * 用户未登录
     */
    NO_LOGIN(100000, "未登录"),
    /**
     * 用户会话已过期
     */
    SESSION_EXPIRED(100001, "会话已过期"),
    /**
     * 用户无访问权限
     */
    PERMISSION_NO_ACCESS(100002, "无访问权限"),
    /**
     * 非法请求
     */
    ILLEGAL_REQUEST(100003, "非法请求"),
    /**
     * 系统异常
     */
    SERVER_EXCEPTION(100004, "系统繁忙，请稍后重试"),
    /**
     * 签名非法
     */
    SIGN_ILLEGAL(100005, "非法签名"),
    /**
     * 参数为空，需传入调用format函数传入参数
     */
    PARAM_BLANK(100010, "参数为空: %s"),
    /**
     * 参数无效、格式不正确，需传入调用format函数传入参数
     */
    PARAM_INVALID(100011, "参数无效: %s"),
    /**
     * 参数格式不正确，需传入调用format函数传入参数
     */
    PARAM_BAD_FORMAT(100012, "参数格式不正确: %s"),
    /**
     * 参数类型错误，需传入调用format函数传入参数
     */
    PARAM_TYPE_ERROR(100013, "参数类型错误: %s"),
    /**
     * 验证码不正确
     */
    VCODE_ERROR(100014, "验证码不正确"),
    /**
     * 不支持的文件类型
     */
    NOT_SUPPORT_FILE_TYPE(100020, "不支持的文件类型"),
    /**
     * 超出文件的最大限制
     */
    EXCEED_FILE_MAX_SIZE(100021, "超出文件的最大限制"),
    /**
     * 不正确的文件结尾
     */
    NOT_NORMAL_END(100022, "不正确的文件结尾");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }

    public ResultCode format(Object... msgArgs) {
        this.message = String.format(this.message, msgArgs);
        return this;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name() + " : " + this.code + ", " + this.message;
    }

    public static void main(String[] args) {
        ResultCode result = ResultCode.PARAM_BAD_FORMAT.format("username");
        System.out.println(result);
    }
}
