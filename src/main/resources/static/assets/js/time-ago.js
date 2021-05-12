const MONTH_NAMES = [
  'tháng 1', 'tháng 2', 'tháng 3', 'tháng 4', 'tháng 5', 'tháng 6',
  'tháng 7', 'tháng 8', 'tháng 9', 'tháng 10', 'tháng 11', 'tháng 12'
];


function getFormattedDate(date, prefomattedDate = false, hideYear = false) {
  const day = date.getDate();
  const month = MONTH_NAMES[date.getMonth()];
  const year = date.getFullYear();
  const hours = date.getHours();
  let minutes = date.getMinutes();

  if (minutes < 10) {
    // Adding leading zero to minutes
    minutes = `0${ minutes }`;
  }

  if (prefomattedDate) {
    // Today at 10:20
    // Yesterday at 10:20
    return `${ prefomattedDate }`; // lúc ${ hours }:${ minutes }
  }

  if (hideYear) {
    // 10. January at 10:20
    return `${day} ${month}` // lúc ${hours}:${ minutes };
  }

  // 10. January 2017. at 10:20
  return `${ day } ${ month } ${ year }`; //. lúc ${ hours }:${ minutes }
}


// --- Main function
function timeAgo(dateParam) {
  if (!dateParam) {
    return null;
  }

  const date = typeof dateParam === 'object' ? dateParam : new Date(dateParam);
  const DAY_IN_MS = 86400000; // 24 * 60 * 60 * 1000
  const today = new Date();
  const yesterday = new Date(today - DAY_IN_MS);
  const seconds = Math.round((today - date) / 1000);
  const minutes = Math.round(seconds / 60);
  const isToday = today.toDateString() === date.toDateString();
  const isYesterday = yesterday.toDateString() === date.toDateString();
  const isThisYear = today.getFullYear() === date.getFullYear();


  if (seconds < 5) {
    return 'Vừa xong';
  } else if (seconds < 60) {
    return `${ seconds } giây`;
  } else if (seconds < 90) {
    return '1 phút trước';
  } else if (minutes < 60) {
    return `${ minutes } phút`;
  } else if (isToday) {
    return getFormattedDate(date, 'Hôm nay'); // Today at 10:20
  } else if (isYesterday) {
    return getFormattedDate(date, 'Hôm qua'); // Yesterday at 10:20
  } else if (isThisYear) {
    return getFormattedDate(date, false, true); // 10. January at 10:20
  }

  return getFormattedDate(date); // 10. January 2017. at 10:20
}