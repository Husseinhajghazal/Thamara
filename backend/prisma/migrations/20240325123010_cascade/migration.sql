-- DropForeignKey
ALTER TABLE "Card" DROP CONSTRAINT "Card_deck_id_fkey";

-- DropForeignKey
ALTER TABLE "Deck" DROP CONSTRAINT "Deck_col_id_fkey";

-- DropForeignKey
ALTER TABLE "DeckTag" DROP CONSTRAINT "DeckTag_deck_id_fkey";

-- DropForeignKey
ALTER TABLE "DeckTag" DROP CONSTRAINT "DeckTag_tag_id_fkey";

-- DropForeignKey
ALTER TABLE "Rate" DROP CONSTRAINT "Rate_deck_id_fkey";

-- AddForeignKey
ALTER TABLE "Deck" ADD CONSTRAINT "Deck_col_id_fkey" FOREIGN KEY ("col_id") REFERENCES "Collection"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Card" ADD CONSTRAINT "Card_deck_id_fkey" FOREIGN KEY ("deck_id") REFERENCES "Deck"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Rate" ADD CONSTRAINT "Rate_deck_id_fkey" FOREIGN KEY ("deck_id") REFERENCES "Deck"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DeckTag" ADD CONSTRAINT "DeckTag_deck_id_fkey" FOREIGN KEY ("deck_id") REFERENCES "Deck"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "DeckTag" ADD CONSTRAINT "DeckTag_tag_id_fkey" FOREIGN KEY ("tag_id") REFERENCES "Tag"("id") ON DELETE CASCADE ON UPDATE CASCADE;
