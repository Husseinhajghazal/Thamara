"use client";

import React, { useState } from "react";
import Background from "../popup/Background";
import DeckPopup from "../popup/DeckPopup";
import Button from "../Button";

const DeckHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <Button toggleShow={toggleShow} text="أضف حزمة جديدة" />
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <DeckPopup />}
    </React.Fragment>
  );
};

export default DeckHead;
