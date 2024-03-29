"use client";

import React from "react";
import { FaUnlink } from "react-icons/fa";

const Link = ({ tagName, deckName, tagId, deckId }) => {
  const deleteCollection = () => {
    fetch("https://one18-team.onrender.com/tag/connection", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        tag_id: tagId,
        deck_id: deckId,
      }),
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <React.Fragment>
      <div className="w-full shadow-md bg-white rounded-lg p-4 flex justify-between items-center mb-2">
        <div className="flex">
          <h1 className="text-lg font-semibold">{tagName}</h1>/
          <h1 className="text-lg font-semibold">{deckName}</h1>
        </div>
        <div className="flex gap-2">
          <button
            className="p-2 bg-red-600 rounded-md"
            onClick={deleteCollection}
          >
            <FaUnlink color="white" />
          </button>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Link;
