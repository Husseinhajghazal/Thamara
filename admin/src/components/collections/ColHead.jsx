"use client";

import React, { useState } from "react";
import CollectionPopup from "../popup/CollectionPopup";
import Background from "../popup/Background";

const ColHead = () => {
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
      {show && <CollectionPopup />}
    </React.Fragment>
  );
};

export default ColHead;
