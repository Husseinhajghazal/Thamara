"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";
import { BsCollection } from "react-icons/bs";
import { FaTag } from "react-icons/fa";
import { GiCardBurn } from "react-icons/gi";
import { GiCardAceSpades } from "react-icons/gi";

const sidebarItems = [
  {
    icon: <BsCollection />,
    link: "/",
  },
  {
    icon: <FaTag />,
    link: "/tags",
  },
  {
    icon: <GiCardBurn />,
    link: "/cards",
  },
  {
    icon: <GiCardAceSpades />,
    link: "/deck",
  },
];

const Sidebar = () => {
  const path = usePathname();

  return (
    <div className="w-[80px] shadow-md h-screen rounded-r-lg flex flex-col items-center py-10 bg-green-500">
      {sidebarItems.map((e, index) => (
        <Link
          key={index}
          href={e.link}
          className={`text-white text-3xl py-5 hover:text-gray-200 duration-300 ${
            e.link == path &&
            "bg-white/35 w-full flex justify-center border-l-4 border-white hover:text-white"
          }`}
        >
          {e.icon}
        </Link>
      ))}
    </div>
  );
};

export default Sidebar;
