"use client";

import React, { useState } from "react";
import Background from "../popup/Background";
import LinkPopup from "../popup/LinkPopup";
import Button from "../Button";

const LinkHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <Button toggleShow={toggleShow} text="ربط جديد" />
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <LinkPopup />}
    </React.Fragment>
  );
};

export default LinkHead;
