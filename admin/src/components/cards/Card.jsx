"use client";

import React, { useState } from "react";
import { MdDelete, MdEditSquare } from "react-icons/md";
import Background from "../popup/Background";
import EditCardPopup from "../popup/EditCardPopup";

const Card = ({
  id,
  question,
  answer_type,
  deck_name,
  image_url,
  right_answer,
  deck_id,
  wrongAnswer1,
  wrongAnswer2,
  wrongAnswer3,
}) => {
  const [show, setShow] = useState(false);
  const toggleShow = () => setShow(!show);
  const deleteCollection = () => {
    fetch("https://one18-team.onrender.com/card/" + id, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <React.Fragment>
      <div className="shadow-md bg-white rounded-lg p-4 flex flex-col items-center gap-3">
        <div className="w-full h-[200px] overflow-hidden">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/a/ac/NewTux.png"
            className="object-contain h-full w-full"
          />
        </div>
        <div className="bg-gray-200 w-full p-2 rounded-md">
          <div className="flex items-center gap-1">
            <p className="font-semibold">Question type :</p>
            <p>
              {answer_type == "tf"
                ? "صح أو غلط"
                : answer_type == "s"
                ? "سؤال وجواب"
                : "أختيار من متعدد"}
            </p>
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Related Deck :</p>
            <p>{deck_name}</p>
          </div>
          <div className="flex items-center gap-1">
            <p className="font-semibold">Right answer :</p>
            <p>{right_answer.toString()}</p>
          </div>
        </div>
        <h1 className="text-lg font-semibold">{question}</h1>
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
        <EditCardPopup
          id={id}
          oldQuestion={question}
          oldAnswer_type={answer_type}
          oldImage_url={image_url}
          oldRight_answer={right_answer}
          deck_id={deck_id}
          oldWrongAnswer1={wrongAnswer1}
          oldWrongAnswer2={wrongAnswer2}
          oldWrongAnswer3={wrongAnswer3}
        />
      )}
    </React.Fragment>
  );
};

export default Card;
