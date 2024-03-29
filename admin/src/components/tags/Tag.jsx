"use client";

import React, { useState } from "react";
import { MdDelete, MdEditSquare } from "react-icons/md";
import Background from "../popup/Background";
import EditTagPopup from "../popup/EditTagPopup";

const Tag = ({ name, id }) => {
  const [show, setShow] = useState(false);
  const toggleShow = () => setShow(!show);
  const deleteCollection = () => {
    fetch("https://one18-team.onrender.com/tag/" + id, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <React.Fragment>
      <div className="w-full shadow-md bg-white rounded-lg p-4 flex justify-between items-center mb-2">
        <h1 className="text-lg font-semibold">{name}</h1>
        <div className="flex gap-2">
          <button className="p-2 bg-green-500 rounded-md" onClick={toggleShow}>
            <MdEditSquare color="white" size={20} />
          </button>
          <button
            className="p-2 bg-red-600 rounded-md"
            onClick={deleteCollection}
          >
            <MdDelete color="white" size={20} />
          </button>
        </div>
      </div>
      {show && <Background toggleShow={toggleShow} />}
      {show && <EditTagPopup oldName={name} id={id} />}
    </React.Fragment>
  );
};

export default Tag;
