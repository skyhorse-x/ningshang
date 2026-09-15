import DOMPurify from 'dompurify'

const escapeText = value => value.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;')

// Legacy plain text remains readable and keeps its paragraphs when first edited.
export function richHtml(value) {
  const text = String(value ?? '')
  const html = /<\/?[a-z][^>]*>/i.test(text)
    ? text
    : text.split(/\r?\n\s*\r?\n/).map(p => `<p>${escapeText(p).replace(/\r?\n/g, '<br>')}</p>`).join('')
  return DOMPurify.sanitize(html, { FORBID_TAGS: ['style', 'iframe', 'form'], FORBID_ATTR: ['srcdoc'] })
}

export function richTextPreview(value) {
  const doc = new DOMParser().parseFromString(richHtml(value), 'text/html')
  return (doc.body.textContent || '').trim()
}

export function isRichContentKey(key = '') {
  return /(_body|_description|_intro)$/.test(key) || key.startsWith('culture_') || ['speech_quote', 'footer_brand_desc'].includes(key)
}
