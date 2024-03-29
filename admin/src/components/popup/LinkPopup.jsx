"use client";

import React, { useEffect, useState } from "react";

const LinkPopup = () => {
  const [deck_id, setDeck_id] = useState(0);
  const [tag_id, setTag_id] = useState(0);

  const [decks, setDecks] = useState([]);
  const [tags, setTags] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/deck", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setDecks(data.decks);
      });
    fetch("https://one18-team.onrender.com/tag", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setTags(data.tags);
      });
  }, []);

  const handleSumbit = async (e) => {
    e.preventDefault();
    fetch("https://one18-team.onrender.com/tag/connection", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        deck_id: deck_id,
        tag_id: tag_id,
      }),
    })
      .then((response) => response.json())
      .then((data) => window.location.reload());
  };

  return (
    <div className="bg-white p-6 rounded-md absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center justify-center gap-5 flex-col">
      <h2 className="text-green-500 font-semibold text-3xl">
        Link Deck with Tag
      </h2>
      <form onSubmit={handleSumbit} className="flex flex-col gap-5">
        <select
          onChange={(e) => setDeck_id(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
        >
          <option value={0}>أختر الحزمة</option>
          {decks.map((e, index) => (
            <option key={index} value={e.id}>
              {e.name}
            </option>
          ))}
        </select>
        <select
          onChange={(e) => setTag_id(e.target.value)}
          className="focus:outline-none border-2 w-[500px] p-3 rounded-lg focus:border-green-500 hover:border-green-500 duration-300"
        >
          <option value={0}>أختر التاغ</option>
          {tags.map((e, index) => (
            <option key={index} value={e.id}>
              {e.value}
            </option>
          ))}
        </select>
        <button className="bg-green-500 text-white mx-auto px-6 py-2 rounded-md">
          Link
        </button>
      </form>
    </div>
  );
};

export default LinkPopup;
