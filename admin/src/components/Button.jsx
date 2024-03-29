import React from "react";

const Button = ({ text, toggleShow }) => {
  return (
    <button
      onClick={toggleShow}
      className="bg-green-500 text-white px-6 py-2 rounded-lg"
    >
      {text}
    </button>
  );
};

export default Button;
