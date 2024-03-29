"use client";

import React, { useState } from "react";
import CollectionPopup from "../popup/CollectionPopup";
import Background from "../popup/Background";
import Button from "../Button";

const ColHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <Button toggleShow={toggleShow} text="أضف مجموعة جديدة" />
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <CollectionPopup />}
    </React.Fragment>
  );
};

export default ColHead;
