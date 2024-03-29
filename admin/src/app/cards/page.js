import CardsList from "../../components/cards/CardsList";
import CardsHead from "../../components/cards/CardsHead";

export default function Home() {
  return (
    <div className="p-5 w-full">
      <CardsHead />
      <CardsList />
    </div>
  );
}
