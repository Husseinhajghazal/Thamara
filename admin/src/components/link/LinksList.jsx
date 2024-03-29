"use client";

import React, { useEffect, useState } from "react";
import Link from "./Link";

const LinksList = () => {
  const [links, setLinks] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/tag/connection", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setLinks(
          data.links.map((link) => ({
            tag_id: link.tag_id,
            deck_id: link.deck_id,
            tagName: link.tag.value,
            deckName: link.deck.name,
          }))
        );
      });
  }, []);

  return (
    <div className="w-full my-5 grid grid-cols-5 gap-3">
      {links.map((e, index) => (
        <Link
          key={index}
          tagName={e.tagName}
          deckName={e.deckName}
          tagId={e.tag_id}
          deckId={e.deck_id}
        />
      ))}
    </div>
  );
};

export default LinksList;
