export function isWechat() {
    const ua = navigator.userAgent.toLowerCase();
    return ua.indexOf("micromessenger") !== -1;
}

/**
 * 时间戳转换成年月日时分格式
 */
export function formatTime(time: number) {
    const date = new Date(time);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();

    const hour = date.getHours();
    const minute = date.getMinutes();

    // 不够两位数补0
    function addZero(num: number) {
        return num < 10 ? "0" + num : num;
    }


    return `${year}-${addZero(month)}-${addZero(day)} ${addZero(hour)}:${addZero(minute)}`;
}