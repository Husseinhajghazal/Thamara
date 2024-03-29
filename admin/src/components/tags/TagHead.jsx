"use client";

import React, { useState } from "react";
import Background from "../popup/Background";
import TagPopup from "../popup/TagPopup";
import Button from "../Button";

const TagHead = () => {
  const [show, setShow] = useState(false);

  const toggleShow = () => setShow(!show);

  return (
    <React.Fragment>
      <div className="w-full text-right">
        <Button toggleShow={toggleShow} text="أضف تاغ جديد" />
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <TagPopup />}
    </React.Fragment>
  );
};

export default TagHead;
