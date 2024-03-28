import React from "react";

const Background = ({ toggleShow }) => {
  return (
    <div
      className="absolute top-0 left-0 w-screen h-screen bg-[#0000004c]"
      onClick={toggleShow}
    />
  );
};

export default Background;
