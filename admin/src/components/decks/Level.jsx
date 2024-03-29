import React from "react";

const Level = ({ level }) => {
  const percentage = (level / 10) * 100;
  let colorClass;

  if (level >= 7) {
    colorClass = "bg-red-500";
  } else if (level >= 4 && level < 7) {
    colorClass = "bg-orange-500";
  } else {
    colorClass = "bg-green-500";
  }

  return (
    <div className="w-1/2 h-3 bg-gray-200 rounded-full overflow-hidden">
      <div
        className={`h-full ${colorClass}`}
        style={{ width: `${percentage}%` }}
      ></div>
    </div>
  );
};

export default Level;
