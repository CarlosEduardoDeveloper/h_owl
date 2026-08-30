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
    // Legacy aliases
    text: '#1E1B38',
    tint: '#ED5B0A',

    // Core surfaces
    background: '#FFF6E5',
    foreground: '#1E1B38',

    // Cards / elevated surfaces
    card: '#FFFFFF',
    cardForeground: '#1E1B38',

    // Primary action color (buttons, links, active states)
    primary: '#ED5B0A',
    primaryDark: '#C84800',
    primaryForeground: '#FFFFFF',

    // Secondary / less-emphasis interactive surfaces
    secondary: '#FFF6E5',
    secondaryForeground: '#ED5B0A',

    // Muted / subdued elements
    muted: '#F4ECE0',
    mutedForeground: '#8C7C6D',

    // Accent highlights
    accent: '#D7F9EB',
    accentForeground: '#046865',

    // Avatar Teal
    avatarTeal: '#046865',

    // Destructive actions
    destructive: '#D93838',
    destructiveForeground: '#FFFFFF',

    // Borders and input outlines
    border: '#ED5B0A',
    input: '#E8DFC9',
    inputBackground: '#FFFFFF',

    // Stat Cards
    statOfensivaBg: '#FDE3D2',
    statXpBg: '#D7F9EB',
    statRanquingBg: '#EBE4FF',

    // Progress Colors
    orangeProgress: '#ED5B0A',
    purpleProgress: '#7C3AED',
  },

  radius: 16,
};

export default colors;
