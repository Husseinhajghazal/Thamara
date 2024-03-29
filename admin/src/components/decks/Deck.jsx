"use client";

import React, { useState } from "react";
import { MdDelete, MdEditSquare } from "react-icons/md";
import Background from "../popup/Background";
import EditDeckPopup from "../popup/EditDeckPopup";
import StarRating from "./StarRating";
import Level from "./Level";

const Collection = ({
  id,
  name,
  color,
  level,
  allowShuffle,
  col_id,
  rate,
  ratesCounts,
  col_name,
  cardsCount,
}) => {
  const [show, setShow] = useState(false);
  const toggleShow = () => setShow(!show);
  const deleteCollection = () => {
    fetch("https://one18-team.onrender.com/deck/" + id, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <React.Fragment>
      <div className="shadow-md bg-white rounded-lg p-4 flex flex-col items-center gap-3">
        <div className="bg-gray-200 w-full p-2 rounded-md">
          <div className="flex items-center gap-1">
            <p className="font-semibold">Rate :</p>
            <StarRating rating={rate} />
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Rates Count :</p>
            <p>{ratesCounts}</p>
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Cards Count :</p>
            <p>{cardsCount}</p>
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Collection Name :</p>
            <p>{col_name}</p>
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Level :</p>
            <Level level={level} />
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Color :</p>
            <p
              style={{ backgroundColor: color }}
              className="h-3 w-9/12 rounded-md"
            />
          </div>
        </div>
        <h1 className="text-lg font-semibold">{name}</h1>
        <div className="flex gap-4">
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
      {show && (
        <EditDeckPopup
          oldName={name}
          id={id}
          oldColor={color}
          oldLevel={level}
          oldAllowShuffle={allowShuffle}
          oldCol_id={col_id}
        />
      )}
    </React.Fragment>
  );
};

export default Collection;
