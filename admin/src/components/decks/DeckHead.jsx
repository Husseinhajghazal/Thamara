"use client";

import React, { useState } from "react";
import Background from "../popup/Background";
import DeckPopup from "../popup/DeckPopup";

const DeckHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <button
          onClick={toggleShow}
          className="bg-green-500 text-white px-6 py-2 rounded-lg"
        >
          Add New
        </button>
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <DeckPopup />}
    </React.Fragment>
  );
};

export default DeckHead;
