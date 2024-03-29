"use client";

import React, { useState } from "react";
import CardsPopup from "../popup/CardsPopup";
import Background from "../popup/Background";
import Button from "../Button";

const CardsHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <Button toggleShow={toggleShow} text="أضف كرت" />
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <CardsPopup />}
    </React.Fragment>
  );
};

export default CardsHead;
