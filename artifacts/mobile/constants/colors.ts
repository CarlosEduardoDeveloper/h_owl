/**
 * Semantic design tokens for the mobile app.
 *
 * These tokens mirror the naming conventions used in web artifacts (index.css)
 * so that multi-artifact projects share a cohesive visual identity.
 *
 * Replace the placeholder values below with values that match the project's
 * brand. If a sibling web artifact exists, read its index.css and convert the
 * HSL values to hex so both artifacts use the same palette.
 *
 * To add dark mode, add a `dark` key with the same token names.
 * The useColors() hook will automatically pick it up.
 */

const colors = {
  light: {
    // Legacy aliases (kept for backward compatibility)
    text: '#152238',
    tint: '#ef7657',

    // Core surfaces
    background: '#f7f4ee',
    foreground: '#152238',

    // Cards / elevated surfaces
    card: '#fffdf8',
    cardForeground: '#152238',

    // Primary action color (buttons, links, active states)
    primary: '#ef7657',
    primaryForeground: '#ffffff',

    // Secondary / less-emphasis interactive surfaces
    secondary: '#e9e2d5',
    secondaryForeground: '#152238',

    // Muted / subdued elements (dividers, timestamps, placeholders)
    muted: '#ece6dc',
    mutedForeground: '#687286',

    // Accent highlights (badges, selected items, focus rings)
    accent: '#dce9e4',
    accentForeground: '#19483f',

    // Destructive actions (delete, error states)
    destructive: '#c94f4f',
    destructiveForeground: '#ffffff',

    // Borders and input outlines
    border: '#ddd5c9',
    input: '#cfc5b7',
  },

  // Border radius (in px). Sync from the sibling web artifact's --radius
  // CSS variable. This value applies to cards, buttons, inputs, and modals.
  radius: 16,
};

export default colors;
