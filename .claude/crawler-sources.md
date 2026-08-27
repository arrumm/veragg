# Zwangsversteigerung crawl sources

Survey of German foreclosure-auction (Zwangsversteigerung) sources, researched 2026-08-23, for extending `veragg`'s `auction_sources` beyond the existing HANMARK (`WEBSITE`) and Villingen-Schwenningen (`PDF`) rows.

Crawler type legend: **WEBSITE** = data sits in HTML on the page itself, crawlable like `HanmarkCrawler`. **PDF** = the actual term list is a downloadable PDF, crawlable like the Villingen-Schwenningen source. **REDIRECT** = the page has no data of its own, only a pointer to another source — not a crawl target itself. **UNVERIFIED** = found but format not confirmed by fetch.

## Official centralized portal (covers most of the country)

| Source | URL | States / coverage | Crawler type | Notes |
|---|---|---|---|---|
| ZVG-Portal | https://www.zvg-portal.de/ | Bayern, Sachsen, Niedersachsen, Rheinland-Pfalz, Sachsen-Anhalt, Schleswig-Holstein, Thüringen, Bremen (all confirmed exclusive channel) | WEBSITE | Government portal live since 2007; search UI + per-case pages with attached Gutachten/Exposé/photo PDFs. Highest coverage-per-crawler of anything on this list. |
| Justizportal des Bundes und der Länder | https://www.justiz.de/onlinedienste/zwangsversteigerungstermine/index.php | Federal signpost | REDIRECT | Just links to zvg-portal.de. |

## Independent state systems

| Source | URL | State | Crawler type | Notes |
|---|---|---|---|---|
| NRW Amtsgericht template | e.g. https://www.ag-koeln.nrw.de/behoerde/zvg_termine/index.php | Nordrhein-Westfalen | WEBSITE | One template reused per court — same URL pattern `ag-<city>.nrw.de/behoerde/zvg_termine/index.php`. Confirmed on Köln, Düsseldorf, Bonn, Münster, Aachen, Moers. One crawler + a court-name list covers all of NRW. |
| NRW-Justiz PLZ search | https://www.justiz.nrw/JM/online_verfahren_projekte/zwangsversteigerungstermine | Nordrhein-Westfalen | UNVERIFIED | Postal-code search front end; likely queries the same per-court pages above rather than being an independent source. |
| Baden-Württemberg Amtsgericht template | e.g. https://amtsgericht-villingen-schwenningen.justiz-bw.de/pb/,Lde/Startseite/Aktuelles/Zwangsversteigerungen | Baden-Württemberg | PDF (mixed) | Shared `amtsgericht-<city>.justiz-bw.de` CMS. Villingen-Schwenningen (existing seed), Heidelberg, Rottweil, Lörrach, Freiburg, Offenburg, Stuttgart, Stuttgart-Bad Cannstatt use the same template — verify per court, since some (e.g. Heilbronn) redirect to zvg.com instead of hosting their own PDF list. |
| Hessen Amtsgericht template | e.g. https://ordentliche-gerichtsbarkeit.hessen.de/landgerichtsbezirk-frankfurt-am-main/amtsgericht-frankfurt-am-main/versteigerungstermine | Hessen | UNVERIFIED | Same `ordentliche-gerichtsbarkeit.hessen.de` template per court (Frankfurt, Darmstadt, Wiesbaden, Seligenstadt, Friedberg, Rüsselsheim found). Format not fetch-confirmed; several of these courts also appear on zvg-online.net. |

## Confirmed redirect-only courts (no unique data — skip as crawl targets)

| Court | URL | Redirects to |
|---|---|---|
| AG Heilbronn (BW) | https://amtsgericht-heilbronn.justiz-bw.de/pb/,Lde/Startseite/Aktuelles/Zwangsversteigerungstermine | zvg.com |
| AG Augsburg (Bayern) | https://www.justiz.bayern.de/gerichte-und-behoerden/amtsgerichte/augsburg/verfahren_28.php | zvg-portal.de |
| AG Dresden (Sachsen) | https://www.justiz.sachsen.de/agdd/zwangsversteigerung-4217.html | zvg-portal.de |
| AG Hannover (Niedersachsen) | https://amtsgericht-hannover.niedersachsen.de/startseite/service/wir_fur_sie/zwangsversteigerung_und_verwaltung/zwangsversteigerungen-des-amtsgerichts-hannover-63665.html | zvg-portal.de |
| AG Neustadt a.d. Weinstraße (Rheinland-Pfalz) | https://agnw.justiz.rlp.de/service-informationen/zwangsversteigerung | zvg-portal.de (state-wide since 01.01.2023) |
| AG Halle/Saale (Sachsen-Anhalt) | https://ag-hal.sachsen-anhalt.de/service/zwangsversteigerungsverfahren | zvg-portal.de |
| AG Ahrensburg (Schleswig-Holstein) | https://www.schleswig-holstein.de/DE/justiz/gerichte-und-justizbehoerden/AmtsgerichteSH/AGAhrensburg/Aufgaben/ZwangsverstVerwsachen/zwangsversteigerung_node | zvg-portal.de, zvg.com |
| Thüringer Oberlandesgericht Infothek | https://gerichte.thueringen.de/infothek/zwangsversteigerungen | zvg-portal.de |
| AG Bremen | https://www.amtsgericht.bremen.de/abteilungen/zwangsversteigerung-1674 | zvg-portal.de |

## Private / commercial nationwide aggregators

| Source | URL | Cost | Crawler type | Notes |
|---|---|---|---|---|
| ZVG-Online.net | https://www.zvg-online.net/ | Free | WEBSITE | Per-state/per-court URL structure (`/<state-id>/<court-id>/ag_seite_001.php`). Confirmed serving Berlin, Bayern (München), Hessen (Frankfurt, Kassel), Brandenburg, Saarland, Sachsen-Anhalt (Magdeburg, Halle). |
| Zvg24 | https://www.zvg24.net/ | Free | WEBSITE | 475 courts, includes Gutachten/photos, map + court navigation. |
| Bundesversteigerungsportal | https://bundesversteigerungsportal.de/ | Freemium (paid PDF calendar add-on) | WEBSITE | 3,447 active listings; also covers Austria/Switzerland. |
| Versteigerungskalender | https://www.versteigerungskalender.de/ | Freemium | WEBSITE | 3,700+ listings, one page per Bundesland. |
| zwangsversteigerung.de | https://www.zwangsversteigerung.de/amtsgericht/bundesland | Free | WEBSITE | Directory of every Amtsgericht by state — useful as a court-URL index even if not crawled for auction data itself. |
| Versteigerungspool | https://versteigerungspool.de/ | Free (unverified in depth) | WEBSITE | Per-court pages, e.g. Berlin-Schöneberg, Bremen, Saarbrücken. |
| Argetra | https://www.argetra.de/ | Paid subscription (Plus6/12/24 tiers) | WEBSITE | Positions itself as market leader; HTML per-court listings behind a paywall for full data. |
| zvgsuche.de | https://zvgsuche.de/ | Free | WEBSITE | 3,796 listings across all 16 states, surfaces Gutachten. |
| ZVG.com | https://www.zvg.com/ | Unverified | UNVERIFIED | Explicit redirect target named by AG Heilbronn; content not fetched (metadata only). |
| zvg-pilot.de | https://www.zvg-pilot.de/ | Unverified | UNVERIFIED | Found via search, not deep-checked. |
| zvg-termine.de | https://www.zvg-termine.de/ | Unverified | UNVERIFIED | Found via search, not deep-checked. |
| zvgscout.com | https://zvgscout.com/ | Unverified | UNVERIFIED | Found via search, not deep-checked. |
| portal-zvg.de | https://www.portal-zvg.de/ | Unverified | UNVERIFIED | Found via search, not deep-checked. |

## Suggested build order

1. **ZVG-Portal crawler** (WEBSITE) — single highest-coverage target, feeds 8+ states.
2. **NRW Amtsgericht crawler** (WEBSITE) — one template, parameterize by court name.
3. **Baden-Württemberg PDF crawler** — extend the existing pattern; verify each `justiz-bw.de` court individually before adding as a `PDF` source, since some redirect instead of hosting their own list.
4. Evaluate **zvg24.net** or **zvgsuche.de** as a free alternative to crawling hundreds of individual court pages one by one, since both already aggregate nationwide in one consistent HTML format.
